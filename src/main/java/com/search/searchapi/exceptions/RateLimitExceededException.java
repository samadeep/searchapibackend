package com.search.searchapi.exceptions;

public class RateLimitExceededException extends SearchException {
    public RateLimitExceededException(String source) {
        super("RATE_LIMIT_EXCEEDED", "Rate limit exceeded for source " + source);
    }
} 