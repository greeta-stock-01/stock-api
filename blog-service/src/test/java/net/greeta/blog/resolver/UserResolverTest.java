package net.greeta.blog.resolver;

import graphql.Assert;
import net.greeta.blog.entity.UserEntity;
import net.greeta.blog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResolverTest {

    @LocalServerPort
    private int randomServerPort;

    private UserRepository userRepository;

    private HttpGraphQlTester graphQlTesterForUnsecureOperations;
    private HttpGraphQlTester graphQlTesterForSecureOperations;

    @BeforeEach
    public void setup() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + randomServerPort + "/graphql")
                .responseTimeout(Duration.ofMinutes(2L))
                .build();

        graphQlTesterForUnsecureOperations = HttpGraphQlTester.create(client);

        graphQlTesterForSecureOperations = graphQlTesterForUnsecureOperations
                .mutate()
                .headers(headers -> headers.set("Authorization", SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_VIEWER")).toString()
                ))
                .build();
    }

    @Test
    public void shouldAllowToCreateNewUser() {
        // language=GraphQL
        String createUserMutation = """
            mutation {
              addUser(addUserInput: {
              name: "Vikas",
              password: "pass",
              roles: "ROLE_VIEWER"
             })
            }
        """.trim();

        List<UserEntity> users = userRepository.findAll();
        Assertions.assertThat(users.size()).isEqualTo(0);

        String userId = graphQlTesterForUnsecureOperations.document(createUserMutation)
                .execute()
                .path("addUser")
                .entity(String.class)
                .get();

        List<UserEntity> newUserList = userRepository.findAll();
        Assertions.assertThat(newUserList.size()).isEqualTo(1);

        Assertions.assertThat(newUserList.stream()
                .filter(user -> user.getId().equals(UUID.fromString(userId)))
                .findFirst()
                .orElseThrow()
                .getName()).isEqualTo("Vikas");
    }

    @Test
    public void shouldAllowUserToLoginWithValidCred() {
        // language=GraphQL
        String createUserMutation = """
            mutation {
              addUser(addUserInput: {
              name: "Vikas",
              password: "pass",
              roles: "ROLE_VIEWER"
             })
            }
        """.trim();

        graphQlTesterForUnsecureOperations.document(createUserMutation).executeAndVerify();

        // language=GraphQL
        String loginMutation = """
            mutation login(${'$'}username: String!, ${'$'}password: String!) {
              login(username: ${'$'}username, password: ${'$'}password)
            }
        """.trim();

        String token = graphQlTesterForUnsecureOperations.document(loginMutation)
                .variable("username", "Vikas")
                .variable("password", "pass")
                .execute()
                .path("login")
                .entity(String.class)
                .get();

        Assert.assertNotNull(token);
    }

    @Test
    public void shouldAllowToPerformGetUserQuery() {
        // language=GraphQL
        String createUserMutation = """
            mutation {
              addUser(addUserInput: {
              name: "Vikas",
              password: "pass",
              roles: "ROLE_VIEWER"
             })
            }
        """.trim();

        graphQlTesterForUnsecureOperations.document(createUserMutation).executeAndVerify();

        List<UserEntity> users = userRepository.findAll();
        Assertions.assertThat(users.size()).isEqualTo(1);

        // language=GraphQL
        String getUserQuery = """
            query getUsers(${'$'}page: Int!, ${'$'}size: Int!) {
              getUsers(page: ${'$'}page, size: ${'$'}size) {
                name
              }
            }
        """.trim();

        List<UserEntity> getUsersResponse = graphQlTesterForSecureOperations
                .document(getUserQuery)
                .variable("page", 0)
                .variable("size", 1)
                .execute()
                .path("getUsers")
                .entityList(UserEntity.class)
                .get();

        Assertions.assertThat(getUsersResponse.size()).isEqualTo(1);
        Assertions.assertThat(getUsersResponse.get(0).getName()).isEqualTo("Vikas");
    }
}