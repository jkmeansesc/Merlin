package org.haifan.merlin.config;

import org.haifan.merlin.constants.Provider;

public class GeminiConfig extends LlmConfig {

    public GeminiConfig() {
        super(Provider.GOOGLE_GEMINI);
        load(Provider.GOOGLE_GEMINI);
    }

    public GeminiConfig(String apiKey) {
        super(Provider.GOOGLE_GEMINI, apiKey);
        load(Provider.GOOGLE_GEMINI);
    }

    public GeminiConfig(String configPath, boolean isConfigPath) {
        super(Provider.GOOGLE_GEMINI);
        if (isConfigPath) {
            load(configPath);
        } else {
            throw new IllegalArgumentException("Use GeminiConfig(String apiKey) for setting API key");
        }
    }

    public GeminiConfig(String apiKey, String configPath) {
        super(Provider.GOOGLE_GEMINI, apiKey);
        load(configPath);
    }
}
