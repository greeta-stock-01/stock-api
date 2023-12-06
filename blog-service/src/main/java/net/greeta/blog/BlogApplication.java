package net.greeta.blog;

import net.greeta.blog.entity.CommentEntity;
import net.greeta.blog.entity.PostEntity;
import net.greeta.blog.entity.UserEntity;
import net.greeta.blog.repository.CommentRepository;
import net.greeta.blog.repository.PostRepository;
import net.greeta.blog.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.List;

@EnableMethodSecurity(securedEnabled = true)
@SpringBootApplication
public class BlogApplication {

	@Profile("!test")
	@Bean
	public ApplicationRunner runner(
			UserRepository userRepository,
			PostRepository postRepository,
			CommentRepository commentRepository
	) {
		return args -> {
			UserEntity user = new UserEntity(
					"John",
					"ROLE_ADMIN"
			);

			userRepository.save(user);

			PostEntity postEntity = new PostEntity(
					"Test title",
					"Test description",
					user
			);

			PostEntity postEntity2 = new PostEntity(
					"Test title - 2",
					"Test description - 2",
					user
			);

			postRepository.saveAll(List.of(
					postEntity2,
					postEntity
			));

			CommentEntity comment = new CommentEntity(
					"testing comment",
					user,
					postEntity
			);

			commentRepository.save(comment);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}