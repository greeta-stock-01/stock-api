package net.greeta.blog.resolver;

import net.greeta.blog.entity.PostEntity;
import net.greeta.blog.entity.UserEntity;
import net.greeta.blog.repository.PostRepository;
import net.greeta.blog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.List;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostResolverTest {

    @LocalServerPort
    private int randomServerPort;
    private UserRepository userRepository;
    private PostRepository postRepository;

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
                .headers(headers -> headers.set("Authorization",
                        SecurityMockServerConfigurers.mockJwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_VIEWER")).toString()
                ))
                .build();

        UserEntity user = userRepository.save(new UserEntity("Vikas", "ROLE_VIEWER"));
        postRepository.save(new PostEntity("some title", "some description", user));
    }

    @Test
    public void shouldAllowUserToFetchAllPosts() {
        // language=GraphQL
        String getPostsQuery = """
            query {
              getPosts {
                id
                title
                description
                author {
                   name                                      
                }
              }
            }
        """.trim();

        List<PostEntity> posts = graphQlTesterForSecureOperations.document(getPostsQuery)
                .execute()
                .path("getPosts")
                .entityList(PostEntity.class)
                .get();

        Assertions.assertThat(posts.size()).isEqualTo(1);
        Assertions.assertThat(posts.get(0).getTitle()).isEqualTo("some title");
        Assertions.assertThat(posts.get(0).getDescription()).isEqualTo("some description");
        Assertions.assertThat(posts.get(0).getAuthor().getName()).isEqualTo("Vikas");
    }
}