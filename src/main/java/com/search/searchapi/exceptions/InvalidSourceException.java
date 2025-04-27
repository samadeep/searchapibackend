package com.search.searchapi.exceptions;

public class InvalidSourceException extends InvalidSearchRequestException {
    public InvalidSourceException(String source) {
        super("Invalid source: " + source);
    }
} 