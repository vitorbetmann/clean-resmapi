package com.vitorbetmann.cleanresmapi.infrastructure;

import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeDuplicateNameException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserTypeDuplicateNameException.class)
    public ProblemDetail handleUserTypeDuplicateNameException(UserTypeDuplicateNameException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(UserTypeNotFoundException.class)
    public ProblemDetail handleUserTypeNotFoundException(UserTypeNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

}
