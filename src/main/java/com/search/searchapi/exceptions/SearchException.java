package com.search.searchapi.exceptions;

public class SearchException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public SearchException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
} 