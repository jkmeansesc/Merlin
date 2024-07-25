package org.haifan.merlin.internal.utils;

/**
 * Custom exception thrown by {@link JsonPrinter} when there is an error during JSON serialization.
 */
public class JsonPrinterException extends RuntimeException {

    public JsonPrinterException(String message, Throwable t) {
        super(message, t);
    }
}
