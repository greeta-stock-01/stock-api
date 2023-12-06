package net.greeta.blog.repository;

import net.greeta.blog.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    List<PostEntity> findAllByAuthorId(UUID authorId);

    PostEntity findByCommentsId(UUID commentId);
}