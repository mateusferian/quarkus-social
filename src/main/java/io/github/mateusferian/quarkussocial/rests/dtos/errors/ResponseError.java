package io.github.mateusferian.quarkussocial.rests.dtos.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;
    private String message;
    private Collection<FieldError> errors;

    public static <T>ResponseError createFromValidation(
                    Set<ConstraintViolation<T>> violation){
        List<FieldError> errors = violation
                .stream()
                .map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());

        String message = "Validation error";

        var responseError = new ResponseError(message,errors);

        return responseError;
    }

    public Response withStatusCode(int code){
        return  Response.status(code).entity(this).build();
    }
}
