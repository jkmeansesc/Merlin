package org.haifan.merlin.config;

import org.haifan.merlin.constants.Provider;
import org.haifan.merlin.utils.ConfigLoader;

public class GeminiConfig extends LlmConfig {

    public GeminiConfig() {
        super(Provider.GOOGLE_GEMINI);
    }

    public GeminiConfig(String apiKey) {
        super(Provider.GOOGLE_GEMINI, apiKey);
    }

    @Override
    public ConfigLoader getConfigLoader() {
        return super.configLoader;
    }

    @Override
    public String getBaseUrl() {
        return super.baseUrl;
    }

    @Override
    public String getApiKey() {
        return super.apiKey;
    }
}
