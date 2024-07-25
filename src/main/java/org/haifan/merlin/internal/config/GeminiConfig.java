package org.haifan.merlin.internal.config;

import org.haifan.merlin.internal.constants.Provider;

public class GeminiConfig extends LlmConfig {

    public GeminiConfig() {
        super(Provider.GOOGLE_GEMINI);
        load(Provider.GOOGLE_GEMINI);
    }

    public GeminiConfig(String token) {
        super(Provider.GOOGLE_GEMINI, token);
        load(Provider.GOOGLE_GEMINI);
    }

    public GeminiConfig(String configPath, boolean isConfigPath) {
        super(Provider.GOOGLE_GEMINI);
        if (isConfigPath) {
            load(configPath);
        } else {
            throw new IllegalArgumentException("Use GeminiConfig(String token) for setting API key");
        }
    }

    public GeminiConfig(String token, String configPath) {
        super(Provider.GOOGLE_GEMINI, token);
        load(configPath);
    }
}
