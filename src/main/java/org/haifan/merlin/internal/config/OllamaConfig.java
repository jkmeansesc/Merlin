package org.haifan.merlin.internal.config;

import org.haifan.merlin.internal.constants.Provider;

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
