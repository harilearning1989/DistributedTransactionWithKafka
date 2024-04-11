package com.web.demo.exception;

import org.hibernate.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ProblemDetail> handleExceptions(TransactionException exception, WebRequest webRequest) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Something Not Found");
        problemDetail.setType(URI.create("https://example.com/something-not-found"));
        problemDetail.setProperty("custom_property", "New Message");
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<ErrorResponse> handleBookmarkNotFoundException(ArrayIndexOutOfBoundsException e) {
        ErrorResponse errorResponse = ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage())
                .title("Bookmark not found")
                .type(URI.create("https://api.bookmarks.com/errors/not-found"))
                .property("errorCategory", "Generic")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
