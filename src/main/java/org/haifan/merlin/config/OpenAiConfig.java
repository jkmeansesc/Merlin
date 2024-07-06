package org.haifan.merlin.config;

import org.haifan.merlin.constants.Provider;
import org.haifan.merlin.utils.ConfigLoader;

/**
 * TODO: add javadoc
 */
public class OpenAiConfig extends LlmConfig {

    public OpenAiConfig() {
        super(Provider.OPENAI);
    }

    public OpenAiConfig(String apiKey) {
        super(Provider.OPENAI, apiKey);
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
