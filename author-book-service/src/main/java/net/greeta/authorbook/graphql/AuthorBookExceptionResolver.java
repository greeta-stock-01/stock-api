package net.greeta.authorbook.graphql;

import net.greeta.authorbook.exception.AuthorNotFoundException;
import net.greeta.authorbook.exception.BookDuplicatedIsbnException;
import net.greeta.authorbook.exception.BookNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class AuthorBookExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        ErrorType errorType = ErrorType.INTERNAL_ERROR;
        if (ex instanceof BookNotFoundException || ex instanceof AuthorNotFoundException) {
            errorType = ErrorType.NOT_FOUND;
        } else if (ex instanceof BookDuplicatedIsbnException) {
            errorType = ErrorType.BAD_REQUEST;
        }

        return GraphqlErrorBuilder.newError(env)
                .message("Resolved error: " + ex.getMessage())
                .errorType(errorType)
                .build();
    }
}
