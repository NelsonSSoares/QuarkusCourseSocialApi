package org.acme.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Data
public class ResponseError {
    private final String message;
    private final Collection<FieldError> errors;


    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations){

        List<FieldError> errorList = violations.stream()
                .map(violation -> new FieldError(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        String message = "Validation error";

        return new ResponseError(message, errorList);

    }


    public Response withStatusCode(int statusCode) {
        return Response.status(statusCode).entity(this).build();
    }



}
