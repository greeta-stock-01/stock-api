package net.greeta.blog.model;

import java.util.UUID;

public class AddCommentDto {

    private final String text;
    private final UUID postId;

    public AddCommentDto(String text, UUID postId) {
        this.text = text;
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public UUID getPostId() {
        return postId;
    }
}
