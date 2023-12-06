package net.greeta.blog.config;

import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

import java.util.Objects;

public class UppercaseDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        GraphQLFieldDefinition fieldDefinition = environment.getFieldDefinition();
        GraphQLFieldsContainer container = environment.getFieldsContainer();
        DataFetcher<?> dataFetcher =  environment.getCodeRegistry().getDataFetcher(container, fieldDefinition);

        DataFetcher wrapDataFetcher = DataFetcherFactories.wrapDataFetcher(dataFetcher,
            (env, value) -> {
            if (Objects.nonNull(value)) {
                return ((String) value).toUpperCase();
            }
        return value;
    });

        FieldCoordinates fieldCoordinates = FieldCoordinates.coordinates(container, fieldDefinition);
        environment.getCodeRegistry().dataFetcher(fieldCoordinates, wrapDataFetcher);

        return fieldDefinition;
    }
}