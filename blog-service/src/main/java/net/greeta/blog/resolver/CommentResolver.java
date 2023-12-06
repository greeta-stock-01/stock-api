package net.greeta.blog.resolver;

import lombok.extern.slf4j.Slf4j;
import net.greeta.blog.model.AddCommentDto;
import net.greeta.blog.model.Comment;
import net.greeta.blog.model.Post;
import net.greeta.blog.model.User;
import net.greeta.blog.service.CommentService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class CommentResolver {

    private final CommentService commentService;

    public CommentResolver(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @QueryMapping
    public List<Comment> getComments(@Argument("page") int page, @Argument int size) {
        return commentService.getComments(page, size);
    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @BatchMapping
    public Map<Post, List<Comment>> comments(List<Post> posts) {
        log.info("fetching comments for postIds: (" + posts.stream().map(Post::getId).toList() + ")");
        return commentService.getCommentsByPosts(posts);
    }

//    @SchemaMapping(typeName = "Post")
//    public List<Comment> comments(Post post) {
//        LOGGER.info("Fetching comments for postId: " + post.getId());
//        return commentService.getCommentsByPostId(post.getId());
//    }

    @PreAuthorize("hasAnyRole('BLOG_USER', 'BLOG_MANAGER')")
    @SchemaMapping(typeName = "User")
    public List<Comment> comments(User user) {
        return commentService.getCommentsByUserId(user.getId());
    }

    @PreAuthorize("hasRole('BLOG_MANAGER')")
    @MutationMapping
    public Comment addComment(@Argument("addCommentInput") AddCommentDto addComment) {
        return commentService.addComment(addComment);
    }


}