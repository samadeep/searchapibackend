package com.search.searchapi.exceptions;

public class InvalidPaginationException extends InvalidSearchRequestException {
    public InvalidPaginationException(String message) {
        super(message);
    }
} 