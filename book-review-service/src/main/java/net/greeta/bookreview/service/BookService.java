package net.greeta.bookreview.service;

import net.greeta.bookreview.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getBooks();

    Book validateAndGetBookById(String id);

    Book saveBook(Book book);

    void deleteBook(Book book);

    Book validateAndGetBookByIsbn(String isbn);
}
