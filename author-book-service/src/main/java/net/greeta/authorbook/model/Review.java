package net.greeta.authorbook.model;

public record Review(String reviewer, String comment, Integer rating, String createdAt) {
}
