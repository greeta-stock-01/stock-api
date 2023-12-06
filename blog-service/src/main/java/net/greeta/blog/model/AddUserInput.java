package net.greeta.blog.model;

public class AddUserInput {
    private final String name;
    private final String roles;

    public AddUserInput(String name, String roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public String getRoles() {
        return roles;
    }
}
