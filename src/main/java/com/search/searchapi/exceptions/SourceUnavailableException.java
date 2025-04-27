package com.search.searchapi.exceptions;

public class SourceUnavailableException extends SearchException {
    public SourceUnavailableException(String source) {
        super("SOURCE_UNAVAILABLE", "Search source " + source + " is currently unavailable");
    }
} 