package net.greeta.blog.resolver;

import net.greeta.blog.model.Event;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class HelloWordResolver {

    @QueryMapping
    public String test(
            @Argument LocalDate date,
            @Argument OffsetDateTime bornAt,
            @Argument String phoneNumber,
            @Argument String email) {
        return "date: " + date + ", bornAt: " + bornAt;
    }

    @QueryMapping
    public String helloworld() {
        return "Hello World!";
    }

    @QueryMapping
    public String greet(@Argument String name) {
        return "Hello " + name;
    }

    @QueryMapping
    public List<Integer> getRandomNumbers() {
        return List.of(1, 2, 3);
    }

    @QueryMapping
    public Event getEvent() {
        return new Event(
                UUID.randomUUID(),
                "TESTING"
        );
    }

    @QueryMapping
    public String validationCheck(
            @Argument String name,
            @Argument List<Integer> list,
            @Argument String email) {
        return "Works " + name + " " + list + " " + email;
    }


}