package org.haifan.merlin.config;

import org.haifan.merlin.constants.Provider;

public class OllamaConfig extends LlmConfig {

    public OllamaConfig() {
        super(Provider.OLLAMA);
        load(Provider.OLLAMA);
    }

    public OllamaConfig(String configPath) {
        super(Provider.OLLAMA);
        load(configPath);
    }
}
