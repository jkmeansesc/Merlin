package org.haifan.merlin.config;

import org.haifan.merlin.constants.Provider;
import org.haifan.merlin.utils.ConfigLoader;

public class OllamaConfig extends LlmConfig {

    public OllamaConfig() {
        super(Provider.OLLAMA);
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
