package org.haifan.merlin.internal.utils;

import org.haifan.merlin.internal.constants.Provider;

/**
 * Utility class for managing API keys for various providers.
 * <p>
 * This class is designed to retrieve API keys from environment variables.
 * The class cannot be instantiated.
 */
public class ApiKeyManager {

    private ApiKeyManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Retrieves the API key for the specified provider.
     * <p>
     * The method looks for an environment variable named {@code <PROVIDER>_KEY}, where
     * {@code <PROVIDER>} is the uppercase version of the provided provider name. If the environment
     * variable is found and not empty, its value is returned. If the provider is {@code OLLAMA} and
     * no key is found, the method returns {@code null}. Otherwise, an {@code IllegalStateException}
     * is thrown.
     *
     * @param provider the name of the provider for which to retrieve the API key
     * @return the API key for the specified provider, or {@code null} if the provider is {@code OLLAMA}
     *         and no key is found
     * @throws IllegalStateException if no API key is provided for the specified provider and the provider
     *                               is not {@code OLLAMA}
     */
    public static String getApiKey(String provider) {
        String envVarName = provider.toUpperCase() + "_KEY";
        String envKey = System.getenv(envVarName);
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey;
        } else if (provider.equals(Provider.OLLAMA.name())) {
            return null;
        }

        throw new IllegalStateException("No API key provided for " + provider +
                ". Please supply a key or set the " + envVarName + " environment variable.");
    }
}
