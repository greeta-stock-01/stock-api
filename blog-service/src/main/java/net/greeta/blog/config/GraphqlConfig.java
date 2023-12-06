package net.greeta.blog.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.util.regex.Pattern;

@Configuration
public class GraphqlConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return new RuntimeWiringConfigurer() {
            @Override
            public void configure(RuntimeWiring.Builder builder) {
                builder.scalar(ExtendedScalars.Date)
                        .scalar(ExtendedScalars.DateTime)
                        .scalar(
                                ExtendedScalars.newRegexScalar(
                                        "PhoneNumber"
                                ).addPattern(Pattern.compile("\\([0-9]*\\)[0-9]*")).build()
                        ).scalar(email())
                        .directiveWiring(validationSchemaWiring())
                        .directive("Uppercase", new UppercaseDirective());
            }
        };
    }

    public ValidationSchemaWiring validationSchemaWiring() {
        ValidationRules validationRule = ValidationRules.newValidationRules()
                .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                .build();

        return new ValidationSchemaWiring(validationRule);
    }

    public GraphQLScalarType email() {
        return GraphQLScalarType.newScalar()
                .name("Email")
                .description("String as Email with validation")
                .coercing(new EmailScalar())
                .build();
    }
}