package net.greeta.authorbook.model;

import java.util.List;

public record BookReview(String error, String id, List<Review> reviews) {
}
