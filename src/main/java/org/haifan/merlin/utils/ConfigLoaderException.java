package org.haifan.merlin.utils;

/**
 * Exception thrown to indicate errors during configuration loading.
 * <p>
 * {@code ConfigLoaderException} is a runtime exception thrown when there is a failure
 * to load or parse configuration data. This exception is unchecked.
 * </p>
 */
public class ConfigLoaderException extends RuntimeException {

    public ConfigLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
