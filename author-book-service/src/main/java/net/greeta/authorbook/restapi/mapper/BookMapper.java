package net.greeta.authorbook.restapi.mapper;

import net.greeta.authorbook.model.Book;
import net.greeta.authorbook.restapi.dto.BookResponse;
import net.greeta.authorbook.restapi.dto.CreateBookRequest;
import net.greeta.authorbook.restapi.dto.UpdateBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        implementationName = "RestApiBookMapper",
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper {

    BookResponse toBookResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "reviewRes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Book toBook(CreateBookRequest createBookRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "reviewRes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateBookFromRequest(UpdateBookRequest updateBookRequest, @MappingTarget Book book);
}
