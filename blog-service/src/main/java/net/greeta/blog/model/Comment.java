package net.greeta.blog.model;

import java.util.UUID;

public class Comment {

    private final UUID id;
    private final String text;

    public Comment(UUID id, String text) {
        this.id = id;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
