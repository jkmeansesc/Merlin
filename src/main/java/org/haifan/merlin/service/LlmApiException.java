package org.haifan.merlin.service;

public class LlmApiException extends RuntimeException {
    private final int statusCode;
    private final String errorBody;

    public LlmApiException(int statusCode, String message, String errorBody) {
        super(message);
        this.statusCode = statusCode;
        this.errorBody = errorBody;
    }

    public String errorBody() {
        return this.errorBody;
    }

    public int statusCode() {
        return this.statusCode;
    }
}

