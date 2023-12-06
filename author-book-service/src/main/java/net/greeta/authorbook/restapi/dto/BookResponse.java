package net.greeta.authorbook.restapi.dto;

public record BookResponse(Long id, String isbn, String title, Integer year) {
}
