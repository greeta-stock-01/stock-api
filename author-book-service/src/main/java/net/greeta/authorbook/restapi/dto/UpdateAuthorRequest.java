package net.greeta.authorbook.restapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateAuthorRequest {

    @Schema(example = "Ivan Franchin")
    private String name;
}
