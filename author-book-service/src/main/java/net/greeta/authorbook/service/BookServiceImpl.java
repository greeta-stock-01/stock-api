package net.greeta.authorbook.service;

import net.greeta.authorbook.exception.BookDuplicatedIsbnException;
import net.greeta.authorbook.exception.BookNotFoundException;
import net.greeta.authorbook.model.Author;
import net.greeta.authorbook.model.Book;
import net.greeta.authorbook.repository.BookRepository;
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
    public List<Book> getBooksByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book validateAndGetBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(String.format("Book with id '%s' not found", id)));
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
}
