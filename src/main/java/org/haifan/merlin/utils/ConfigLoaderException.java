package org.haifan.merlin.utils;

/**
 * Custom Exception thrown to indicate errors during configuration loading.
 */
public class ConfigLoaderException extends RuntimeException {

    public ConfigLoaderException(String message, Throwable t) {
        super(message, t);
    }
}
