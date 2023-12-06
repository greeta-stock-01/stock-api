package net.greeta.bookreview.service;

import net.greeta.bookreview.exception.BookDuplicatedIsbnException;
import net.greeta.bookreview.exception.BookNotFoundException;
import net.greeta.bookreview.model.Book;
import net.greeta.bookreview.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book validateAndGetBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with id %s not found", id)));
    }

    @Override
    public Book saveBook(Book book) {
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new BookDuplicatedIsbnException(String.format("Book with ISBN '%s' already exists", book.getIsbn()));
        }
    }

    @Override
    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public Book validateAndGetBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with isbn %s not found", isbn)));
    }
}
