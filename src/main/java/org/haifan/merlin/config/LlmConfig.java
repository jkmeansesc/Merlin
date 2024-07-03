package org.haifan.merlin.config;

import org.haifan.merlin.Provider;
import org.haifan.merlin.utils.ApiKeyManager;
import org.haifan.merlin.utils.ConfigLoader;

/**
 * TODO: add javadoc
 */
public abstract class LlmConfig {
    protected final ConfigLoader configLoader;
    protected final String baseUrl;
    protected final String apiKey;

    protected LlmConfig(Provider provider) {
        this.configLoader = new ConfigLoader();
        this.baseUrl = configLoader.getProperty(provider.name().toLowerCase() + ".baseUrl");
        this.apiKey = ApiKeyManager.getApiKey(provider.name());
    }

    protected LlmConfig(Provider provider, String apiKey) {
        this.configLoader = new ConfigLoader();
        this.baseUrl = configLoader.getProperty(provider.name().toLowerCase() + ".baseUrl");
        this.apiKey = apiKey;
    }

    public abstract ConfigLoader getConfigLoader();

    public abstract String getBaseUrl();

    public abstract String getApiKey();
}
