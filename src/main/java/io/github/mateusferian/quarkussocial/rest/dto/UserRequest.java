package io.github.mateusferian.quarkussocial.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest  {

    @NotBlank(message = "{white.field}")
    @Size(min = 3,max = 50, message = "{size.invalid}")
    private String name;

    @NotNull(message = "{null.field}")
    @Min(value = 18, message = "{below.the.minimum}")
    @Max(value = 70, message = "{above.the.maximum}")
    private int age;
}
