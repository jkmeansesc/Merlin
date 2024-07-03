package org.haifan.merlin.utils;

/**
 * TODO: add javadoc
 */
public class ApiKeyManager {

    private ApiKeyManager() {
        throw new IllegalStateException("Utility class");
    }

    public static String getApiKey(String providerName) {
        String envVarName = providerName.toUpperCase() + "_KEY";
        String envKey = System.getenv(envVarName);
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey;
        }

        throw new IllegalStateException("No API key provided for " + providerName +
                ". Please supply a key or set the " + envVarName + " environment variable.");
    }

}
