package org.merlin.service;

/**
 * Exception thrown when an error occurs during media upload.
 */
public class UploadMediaException extends RuntimeException {

    /**
     * Constructs a new UploadMediaException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public UploadMediaException(String message, Throwable cause) {
        super(message, cause);
    }
}
