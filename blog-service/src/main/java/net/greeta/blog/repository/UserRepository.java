package net.greeta.blog.repository;

import net.greeta.blog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByPostsId(UUID postId);

    UserEntity findByCommentsId(UUID commentId);

    UserEntity findByName(String username);
}