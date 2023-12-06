package net.greeta.blog.service;

import net.greeta.blog.entity.CommentEntity;
import net.greeta.blog.entity.PostEntity;
import net.greeta.blog.entity.UserEntity;
import net.greeta.blog.model.AddCommentDto;
import net.greeta.blog.model.Comment;
import net.greeta.blog.model.Post;
import net.greeta.blog.repository.CommentRepository;
import net.greeta.blog.repository.PostRepository;
import net.greeta.blog.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(
            CommentRepository commentRepository,
            UserRepository userRepository,
            PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<Comment> getComments(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CommentEntity> commentEntities = commentRepository.findAll(pageRequest);
        return commentEntities.stream()
                .map(commentEntity -> new Comment(commentEntity.getId(), commentEntity.getText()))
                .collect(Collectors.toList());
    }

    public List<Comment> getCommentsByPostId(UUID postId) {
        if (postId == null) {
            throw new RuntimeException("Post id cannot be null");
        }
        return commentRepository.findAllByPostId(postId).stream()
                .map(commentEntity -> new Comment(commentEntity.getId(), commentEntity.getText()))
                .collect(Collectors.toList());
    }

    public List<Comment> getCommentsByUserId(UUID userId) {
        if (userId == null) {
            throw new RuntimeException("User id cannot be null");
        }
        return commentRepository.findAllByAuthorId(userId).stream()
                .map(commentEntity -> new Comment(commentEntity.getId(), commentEntity.getText()))
                .collect(Collectors.toList());
    }

    public Comment addComment(AddCommentDto addComment) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = Optional.ofNullable(userRepository.findByName(loggedInUsername))
                .orElseThrow(() -> new RuntimeException("User does not exist with id: " + loggedInUsername));

        PostEntity post = postRepository.findById(addComment.getPostId())
                .orElseThrow(() -> new RuntimeException("Post does not exist with id: " + addComment.getPostId()));

        CommentEntity comment = new CommentEntity();
        comment.setText(addComment.getText());
        comment.setAuthor(user);
        comment.setPost(post);

        CommentEntity createdComment = commentRepository.save(comment);

        return new Comment(createdComment.getId(), createdComment.getText());
    }

    public Map<Post, List<Comment>> getCommentsByPosts(List<Post> posts) {
        List<UUID> postIds = posts.stream()
                .map(Post::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<CommentEntity> comments = commentRepository.findAllByPostIds(postIds);

        return posts.stream()
                .collect(Collectors.toMap(
                        post -> post,
                        post -> comments.stream()
                                .filter(comment -> comment.getPost().getId().equals(post.getId()))
                                .map(comment -> new Comment(comment.getId(), comment.getText()))
                                .collect(Collectors.toList())
                ));
    }
}