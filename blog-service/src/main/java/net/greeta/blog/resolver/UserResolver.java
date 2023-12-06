package net.greeta.blog.resolver;

import lombok.extern.slf4j.Slf4j;
import net.greeta.blog.model.AddUserInput;
import net.greeta.blog.model.Comment;
import net.greeta.blog.model.Post;
import net.greeta.blog.model.User;
import net.greeta.blog.service.UserService;
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
public class UserResolver {

    private final UserService userService;

    public UserResolver(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @QueryMapping
    public List<User> getUsers(@Argument int page, @Argument int size) {
        return userService.getUsers(page, size);
    }

    @MutationMapping
    public UUID addUser(@Argument("addUserInput") AddUserInput userInput) {
        return userService.addUser(userInput);
    }

    // field resolver
    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @SchemaMapping(typeName = "Post")
    public User author(Post post) {
        log.info("Fetching author data for postId: {}", post.getId());
        UUID postId = post.getId();
        if (postId == null) {
            throw new RuntimeException("postId cannot be null");
        }
        return userService.findByPostId(postId);
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @SchemaMapping(typeName = "Comment")
    public User author(Comment comment) {
        return userService.findByCommentId(comment.getId());
    }
}



