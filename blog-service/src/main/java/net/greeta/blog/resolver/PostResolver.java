package net.greeta.blog.resolver;

import lombok.extern.slf4j.Slf4j;
import net.greeta.blog.model.AddPost;
import net.greeta.blog.model.Comment;
import net.greeta.blog.model.Post;
import net.greeta.blog.model.User;
import net.greeta.blog.service.PostService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class PostResolver {

    private final PostService postService;

    public PostResolver(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @QueryMapping
    public List<Post> getPosts() {
        log.info("Fetching posts from database");
        return postService.getPosts();
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @QueryMapping
    public List<Post> recentPosts(@Argument int page, @Argument int size) {
        return postService.getPosts(page, size);
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @SchemaMapping(typeName = "User")
    public List<Post> posts(User user) {
        UUID userId = user.getId();
        if (userId == null) {
            throw new RuntimeException("UserId cannot be null");
        }
        return postService.getPostsByAuthor(userId);
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @SchemaMapping(typeName = "User")
    public int totalPost(User user) {
        UUID userId = user.getId();
        if (userId == null) {
            throw new RuntimeException("UserId cannot be null");
        }
        return postService.getPostsByAuthor(userId).size();
    }

    @PreAuthorize("hasRole('BLOG_MANAGER')")
    @MutationMapping
    public Post addPost(@Argument("addPostInput") AddPost addPost) {
        return postService.addPost(addPost);
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @SchemaMapping(typeName = "Comment")
    public Post post(Comment comment) {
        return postService.getPostByCommentId(comment.getId());
    }



}