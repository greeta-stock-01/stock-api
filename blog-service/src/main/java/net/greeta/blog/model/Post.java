package net.greeta.blog.model;

import java.util.UUID;

public class Post {

    private final UUID id;
    private final String title;
    private final String description;

    public Post(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
