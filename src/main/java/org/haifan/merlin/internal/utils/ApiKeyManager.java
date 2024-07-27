package org.haifan.merlin.internal.utils;

import org.haifan.merlin.internal.constants.Provider;

/**
 * TODO: add javadoc
 */
public class ApiKeyManager {

    private ApiKeyManager() {
        throw new IllegalStateException("Utility class");
    }

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
