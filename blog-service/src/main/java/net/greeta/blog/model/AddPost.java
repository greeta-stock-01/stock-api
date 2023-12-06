package net.greeta.blog.model;

public class AddPost {

    private final String title;
    private final String description;

    public AddPost(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
