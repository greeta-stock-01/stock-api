package net.greeta.authorbook.restapi;

import net.greeta.authorbook.client.BookReviewApiClient;
import net.greeta.authorbook.client.BookReviewApiQueryBuilder;
import net.greeta.authorbook.model.Author;
import net.greeta.authorbook.model.Book;
import net.greeta.authorbook.model.BookReview;
import net.greeta.authorbook.restapi.dto.BookResponse;
import net.greeta.authorbook.restapi.dto.CreateBookRequest;
import net.greeta.authorbook.restapi.dto.UpdateBookRequest;
import net.greeta.authorbook.restapi.mapper.BookMapper;
import net.greeta.authorbook.service.AuthorService;
import net.greeta.authorbook.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController("RestApiBookController")
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final BookReviewApiClient bookReviewApiClient;
    private final BookReviewApiQueryBuilder bookReviewApiQueryBuilder;
    private final BookMapper bookMapper;

    @GetMapping
    public List<BookResponse> getBooks() {
        return bookService.getBooks()
                .stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{bookId}")
    public BookResponse getBook(@PathVariable Long bookId) {
        Book book = bookService.validateAndGetBookById(bookId);
        return bookMapper.toBookResponse(book);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest createBookRequest) {
        Author author = authorService.validateAndGetAuthorById(createBookRequest.getAuthorId());
        Book book = bookMapper.toBook(createBookRequest);
        book.setAuthor(author);
        book = bookService.saveBook(book);
        return bookMapper.toBookResponse(book);
    }

    @PutMapping("/{bookId}")
    public BookResponse updateBook(@PathVariable Long bookId, @Valid @RequestBody UpdateBookRequest updateBookRequest) {
        Book book = bookService.validateAndGetBookById(bookId);
        bookMapper.updateBookFromRequest(updateBookRequest, book);
        Long authorId = updateBookRequest.getAuthorId();
        if (authorId != null) {
            Author author = authorService.validateAndGetAuthorById(authorId);
            book.setAuthor(author);
        }
        book = bookService.saveBook(book);
        return bookMapper.toBookResponse(book);
    }

    @DeleteMapping("/{bookId}")
    public BookResponse deleteBook(@PathVariable Long bookId) {
        Book book = bookService.validateAndGetBookById(bookId);
        bookService.deleteBook(book);
        return bookMapper.toBookResponse(book);
    }

    @GetMapping("/{bookId}/reviews")
    public BookReview getBookReviews(@PathVariable Long bookId) {
        Book book = bookService.validateAndGetBookById(bookId);
        String graphQLQuery = bookReviewApiQueryBuilder.getBookReviewQuery(book.getIsbn());
        return bookReviewApiClient.getBookReviews(graphQLQuery).toBookReview();
    }
}
