package ru.praktikum.mainservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.praktikum.mainservice.exception.BadRequestException;
import ru.praktikum.mainservice.exception.NotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    //404
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> userNotFoundHandler(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //400
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandler(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
