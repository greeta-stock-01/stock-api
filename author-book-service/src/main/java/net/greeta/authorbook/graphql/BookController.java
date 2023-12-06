package net.greeta.authorbook.graphql;

import net.greeta.authorbook.client.BookReviewApiClient;
import net.greeta.authorbook.client.BookReviewApiQueryBuilder;
import net.greeta.authorbook.graphql.input.BookInput;
import net.greeta.authorbook.graphql.mapper.BookMapper;
import net.greeta.authorbook.model.Author;
import net.greeta.authorbook.model.Book;
import net.greeta.authorbook.model.BookReview;
import net.greeta.authorbook.service.AuthorService;
import net.greeta.authorbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller("GraphQlBookController")
public class BookController {

    private final AuthorService authorService;
    private final BookService bookService;
    private final BookMapper bookMapper;
    private final BookReviewApiQueryBuilder bookReviewApiQueryBuilder;
    private final BookReviewApiClient bookReviewApiClient;

    @QueryMapping
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

    @QueryMapping
    public Book getBookById(@Argument Long bookId) {
        return bookService.validateAndGetBookById(bookId);
    }

    @MutationMapping
    public Book createBook(@Argument BookInput bookInput) {
        Author author = authorService.validateAndGetAuthorById(bookInput.authorId());
        Book book = bookMapper.toBook(bookInput);
        book.setAuthor(author);
        return bookService.saveBook(book);
    }

    @MutationMapping
    public Book updateBook(@Argument Long bookId, @Argument BookInput bookInput) {
        Book book = bookService.validateAndGetBookById(bookId);
        bookMapper.updateBookFromRequest(bookInput, book);

        Long authorId = bookInput.authorId();
        if (authorId != null) {
            Author author = authorService.validateAndGetAuthorById(authorId);
            book.setAuthor(author);
        }
        return bookService.saveBook(book);
    }

    @MutationMapping
    public Book deleteBook(@Argument Long bookId) {
        Book book = bookService.validateAndGetBookById(bookId);
        bookService.deleteBook(book);
        return book;
    }

    @SchemaMapping(field = "bookReview")
    public BookReview getBookReview(Book book) {
        String graphQLQuery = bookReviewApiQueryBuilder.getBookReviewQuery(book.getIsbn());
        return bookReviewApiClient.getBookReviews(graphQLQuery).toBookReview();
    }
}
