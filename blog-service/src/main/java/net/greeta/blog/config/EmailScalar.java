package net.greeta.blog.config;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;

import java.util.regex.Pattern;

public class EmailScalar implements Coercing<String, String> {

    @Override
    public String serialize(Object dataFetcherResult) {
        if (dataFetcherResult instanceof StringValue) {
            return ((StringValue) dataFetcherResult).getValue();
        }

        throw new RuntimeException("email is not valid");
    }

    @Override
    public String parseValue(Object input) {
        if (input instanceof StringValue) {
            String possibleEmail = ((StringValue) input).getValue();
            if (isValidEmail(possibleEmail)) {
                return possibleEmail;
            }
        }

        throw new CoercingParseValueException("failed to parse");
    }

    @Override
    public String parseLiteral(Object input) {
        if (input instanceof StringValue) {
            String possibleEmail = ((StringValue) input).getValue();
            if (isValidEmail(possibleEmail)) {
                return possibleEmail;
            }
        }

        throw new CoercingParseLiteralException("failed to parse");
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email);
    }
}