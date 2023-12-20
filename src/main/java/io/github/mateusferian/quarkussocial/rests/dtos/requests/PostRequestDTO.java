package io.github.mateusferian.quarkussocial.rests.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class PostRequestDTO {

    @NotBlank(message = "{white.field}")
    @Size(min = 3,max = 150, message = "{size.invalid}")
    private String text;

}
