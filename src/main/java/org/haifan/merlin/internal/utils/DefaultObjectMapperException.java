package org.haifan.merlin.internal.utils;

/**
 * Custom exception thrown by {@link DefaultObjectMapper} when there is an error during JSON serialization.
 */
public class DefaultObjectMapperException extends RuntimeException {

    public DefaultObjectMapperException(String message, Throwable t) {
        super(message, t);
    }
}
