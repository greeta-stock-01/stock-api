package net.greeta.blog.service;

import net.greeta.blog.entity.PostEntity;
import net.greeta.blog.entity.UserEntity;
import net.greeta.blog.model.AddPost;
import net.greeta.blog.model.Post;
import net.greeta.blog.repository.PostRepository;
import net.greeta.blog.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getPosts() {
        return postRepository.findAll().stream()
                .map(postEntity -> new Post(postEntity.getId(), postEntity.getTitle(), postEntity.getDescription()))
                .collect(Collectors.toList());
    }

    public List<Post> getPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return postRepository.findAll(pageRequest).stream()
                .map(postEntity -> new Post(postEntity.getId(), postEntity.getTitle(), postEntity.getDescription()))
                .collect(Collectors.toList());
    }

    public List<Post> getPostsByAuthor(UUID userId) {
        return postRepository.findAllByAuthorId(userId).stream()
                .map(postEntity -> new Post(postEntity.getId(), postEntity.getTitle(), postEntity.getDescription()))
                .collect(Collectors.toList());
    }

    public Post addPost(AddPost addPost) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByName(loggedInUsername);
        if (user == null) {
            throw new RuntimeException("User is not valid, userId: " + loggedInUsername);
        }

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(addPost.getTitle());
        postEntity.setDescription(addPost.getDescription());
        postEntity.setAuthor(user);

        PostEntity createdPost = postRepository.save(postEntity);

        return new Post(createdPost.getId(), createdPost.getTitle(), createdPost.getDescription());
    }

    public Post getPostByCommentId(UUID commentId) {
        if (commentId == null) {
            throw new RuntimeException("CommentId cannot be null");
        }

        PostEntity postEntity = postRepository.findByCommentsId(commentId);
        return new Post(postEntity.getId(), postEntity.getTitle(), postEntity.getDescription());
    }
}