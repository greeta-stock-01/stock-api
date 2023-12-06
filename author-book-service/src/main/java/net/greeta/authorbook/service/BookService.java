package net.greeta.authorbook.service;

import net.greeta.authorbook.model.Author;
import net.greeta.authorbook.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getBooks();

    List<Book> getBooksByAuthor(Author author);

    Book validateAndGetBookById(Long id);

    Book saveBook(Book book);

    void deleteBook(Book book);
}
