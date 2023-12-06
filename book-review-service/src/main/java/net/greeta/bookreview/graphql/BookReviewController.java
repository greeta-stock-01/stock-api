package net.greeta.bookreview.graphql;

import net.greeta.bookreview.graphql.input.BookInput;
import net.greeta.bookreview.graphql.input.ReviewInput;
import net.greeta.bookreview.mapper.BookMapper;
import net.greeta.bookreview.service.BookService;
import net.greeta.bookreview.mapper.ReviewMapper;
import net.greeta.bookreview.model.Book;
import net.greeta.bookreview.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookReviewController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final ReviewMapper reviewMapper;

    @QueryMapping
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

    @QueryMapping
    public Book getBookById(@Argument String bookId) {
        return bookService.validateAndGetBookById(bookId);
    }

    @QueryMapping
    public Book getBookByIsbn(@Argument String bookIsbn) {
        return bookService.validateAndGetBookByIsbn(bookIsbn);
    }

    @MutationMapping
    public Book createBook(@Argument BookInput bookInput) {
        Book book = bookMapper.toBook(bookInput);
        return bookService.saveBook(book);
    }

    @MutationMapping
    public Book updateBook(@Argument String bookId, @Argument BookInput bookInput) {
        Book book = bookService.validateAndGetBookById(bookId);
        bookMapper.updateBookFromInput(bookInput, book);
        return bookService.saveBook(book);
    }

    @MutationMapping
    public Book deleteBook(@Argument String bookId) {
        Book book = bookService.validateAndGetBookById(bookId);
        bookService.deleteBook(book);
        return book;
    }

    @MutationMapping
    public Book addBookReview(@Argument String bookId, @Argument ReviewInput reviewInput) {
        Book book = bookService.validateAndGetBookById(bookId);
        Review review = reviewMapper.toReview(reviewInput);
        book.getReviews().add(0, review);
        return bookService.saveBook(book);
    }
}
