package com.dotoanime.candidatetracker.exception;

import com.dotoanime.candidatetracker.payload.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleUserServiceException(BadRequestException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleProductServiceException(ResourceNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<Object> handleOrderServiceException(UnauthorizedException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), Instant.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
