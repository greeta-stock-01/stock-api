package net.greeta.authorbook.graphql.input;

public record BookInput(String isbn, String title, Integer year, Long authorId) {
}
