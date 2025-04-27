package com.search.searchapi.exception;

import com.search.searchapi.exceptions.*;
import com.search.searchapi.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidSearchRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSearchRequest(
            InvalidSearchRequestException ex, WebRequest request) {
        logger.warn("Invalid search request: {}", ex.getErrorMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSourceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSource(
            InvalidSourceException ex, WebRequest request) {
        logger.warn("Invalid source specified: {}", ex.getErrorMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPagination(
            InvalidPaginationException ex, WebRequest request) {
        logger.warn("Invalid pagination parameters: {}", ex.getErrorMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SourceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleSourceUnavailable(
            SourceUnavailableException ex, WebRequest request) {
        logger.error("Source unavailable: {}", ex.getErrorMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceeded(
            RateLimitExceededException ex, WebRequest request) {
        logger.warn("Rate limit exceeded: {}", ex.getErrorMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        logger.warn("Validation failed: {}", errors);
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                "Validation failed",
                request.getDescription(false).replace("uri=", ""),
                errors.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SearchException.class)
    public ResponseEntity<ErrorResponse> handleSearchException(
            SearchException ex, WebRequest request) {
        logger.error("Search error: {}", ex.getErrorMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                request.getDescription(false).replace("uri=", ""),
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 