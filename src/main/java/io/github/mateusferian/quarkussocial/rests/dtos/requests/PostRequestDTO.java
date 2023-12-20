package io.github.mateusferian.quarkussocial.rests.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class PostRequestDTO {

    private String text;

}
