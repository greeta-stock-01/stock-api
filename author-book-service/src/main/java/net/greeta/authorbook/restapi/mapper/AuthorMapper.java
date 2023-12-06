package net.greeta.authorbook.restapi.mapper;

import net.greeta.authorbook.model.Author;
import net.greeta.authorbook.restapi.dto.AuthorResponse;
import net.greeta.authorbook.restapi.dto.CreateAuthorRequest;
import net.greeta.authorbook.restapi.dto.UpdateAuthorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        implementationName = "RestApiAuthorMapper",
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthorMapper {

    AuthorResponse toAuthorResponse(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Author toAuthor(CreateAuthorRequest createAuthorRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateAuthorFromRequest(UpdateAuthorRequest updateAuthorRequest, @MappingTarget Author author);
}
