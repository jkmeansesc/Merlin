package org.haifan.merlin.service;

/**
 * Exception thrown when an error occurs while parsing a streaming response.
 */
public class StreamParsingException extends RuntimeException {
    /**
     * Constructs a new StreamParsingException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public StreamParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
