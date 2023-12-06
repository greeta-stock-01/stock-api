package net.greeta.bookreview.exception;

public class BookDuplicatedIsbnException extends RuntimeException {

    public BookDuplicatedIsbnException(String message) {
        super(message);
    }
}
