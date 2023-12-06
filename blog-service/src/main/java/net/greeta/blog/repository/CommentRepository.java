package net.greeta.blog.repository;

import net.greeta.blog.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

    List<CommentEntity> findAllByPostId(UUID postId);

    List<CommentEntity> findAllByAuthorId(UUID authorId);

    @Query("select c from CommentEntity c where c.post.id in ?1")
    List<CommentEntity> findAllByPostIds(List<UUID> postIds);
}