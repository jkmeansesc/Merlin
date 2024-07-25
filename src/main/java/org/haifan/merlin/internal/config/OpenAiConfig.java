package org.haifan.merlin.internal.config;

import org.haifan.merlin.internal.constants.Provider;

/**
 * TODO: add javadoc
 */
public class OpenAiConfig extends LlmConfig {

    public OpenAiConfig() {
        super(Provider.OPENAI);
        load(Provider.OPENAI);
    }

    public OpenAiConfig(String token) {
        super(Provider.OPENAI, token);
        load(Provider.OPENAI);
    }

    public OpenAiConfig(String configPath, boolean isConfigPath) {
        super(Provider.OPENAI);
        if (isConfigPath) {
            load(configPath);
        } else {
            throw new IllegalArgumentException("Use OpenAiConfig(String token) for setting API key");
        }
    }

    public OpenAiConfig(String token, String configPath) {
        super(Provider.OPENAI, token);
        load(configPath);
    }
}
