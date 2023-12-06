package net.greeta.bookreview.mapper;

import net.greeta.bookreview.graphql.input.ReviewInput;
import net.greeta.bookreview.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReviewMapper {

    @Mapping(target = "createdAt", ignore = true)
    Review toReview(ReviewInput reviewInput);
}
