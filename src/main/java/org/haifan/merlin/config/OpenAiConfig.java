package org.haifan.merlin.config;

import org.haifan.merlin.constants.Provider;

/**
 * TODO: add javadoc
 */
public class OpenAiConfig extends LlmConfig {

    public OpenAiConfig() {
        super(Provider.OPENAI);
        load(Provider.OPENAI);
    }

    public OpenAiConfig(String apiKey) {
        super(Provider.OPENAI, apiKey);
        load(Provider.OPENAI);
    }

    public OpenAiConfig(String configPath, boolean isConfigPath) {
        super(Provider.OPENAI);
        if (isConfigPath) {
            load(configPath);
        } else {
            throw new IllegalArgumentException("Use OpenAiConfig(String apiKey) for setting API key");
        }
    }

    public OpenAiConfig(String apiKey, String configPath) {
        super(Provider.OPENAI, apiKey);
        load(configPath);
    }
}
