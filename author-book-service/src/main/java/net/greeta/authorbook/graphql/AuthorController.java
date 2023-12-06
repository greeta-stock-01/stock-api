package net.greeta.authorbook.graphql;

import net.greeta.authorbook.graphql.input.AuthorInput;
import net.greeta.authorbook.graphql.mapper.AuthorMapper;
import net.greeta.authorbook.model.Author;
import net.greeta.authorbook.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller("GraphQlAuthorController")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @QueryMapping
    public List<Author> getAuthors() {
        return authorService.getAuthors();
    }

    @QueryMapping
    public Author getAuthorById(@Argument Long authorId) {
        return authorService.validateAndGetAuthorById(authorId);
    }

    @QueryMapping
    public List<Author> getAuthorByName(@Argument String authorName) {
        return authorService.validateAndGetAuthorByName(authorName);
    }

    @MutationMapping
    public Author createAuthor(@Argument AuthorInput authorInput) {
        Author author = authorMapper.toAuthor(authorInput);
        return authorService.saveAuthor(author);
    }

    @MutationMapping
    public Author updateAuthor(@Argument Long authorId, @Argument AuthorInput authorInput) {
        Author author = authorService.validateAndGetAuthorById(authorId);
        authorMapper.updateAuthorFromRequest(authorInput, author);
        return authorService.saveAuthor(author);
    }

    @MutationMapping
    public Author deleteAuthor(@Argument Long authorId) {
        Author author = authorService.validateAndGetAuthorById(authorId);
        authorService.deleteAuthor(author);
        return author;
    }
}
