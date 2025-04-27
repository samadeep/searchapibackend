package com.search.searchapi.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response")
public class ErrorResponse {
    @Schema(description = "HTTP status code")
    private int status;

    @Schema(description = "HTTP status reason")
    private String error;

    @Schema(description = "Detailed error message")
    private String message;

    @Schema(description = "Timestamp of the error")
    private long timestamp;

    public ErrorResponse(int status, String error, String message, long timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
} 