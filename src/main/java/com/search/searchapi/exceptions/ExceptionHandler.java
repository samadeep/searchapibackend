package com.search.searchapi.exceptions;

public class ExceptionHandler {
    public static String handleException(Exception e) {
        return e.getMessage();
    }
}
