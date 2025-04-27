package com.search.searchapi.exceptions;

public class InvalidSearchRequestException extends SearchException {
    public InvalidSearchRequestException(String message) {
        super("INVALID_REQUEST", message);
    }
} 