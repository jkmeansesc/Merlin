package org.haifan.merlin.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.haifan.merlin.constants.Provider;
import org.haifan.merlin.utils.ApiKeyManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO: add javadoc
 */
@Slf4j
@Getter
public abstract class LlmConfig {
    protected final String apiKey;
    protected JsonNode config;

    protected LlmConfig(Provider provider) {
        this(provider, null);
    }

    protected LlmConfig(Provider provider, String apiKey) {
        this.apiKey = (apiKey != null) ? apiKey : ApiKeyManager.getApiKey(provider.name());
    }

    protected void load(Provider provider) {
        String configFile = "config/" + provider.name().toLowerCase() + ".json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Config file not found: " + configFile);
            }
            ObjectMapper mapper = new ObjectMapper();
            this.config = mapper.readTree(inputStream);
            log.info("Loaded config file: {}", configFile);
        } catch (IOException e) {
            log.error("Failed to load config file: {}", configFile, e);
            throw new LlmConfigException("Error loading configuration", e);
        }
    }

    protected void load(String configPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.config = mapper.readTree(new java.io.File(configPath));
            log.info("Loaded custom config file: {}", configPath);
        } catch (IOException e) {
            log.error("Failed to load custom config file: {}", configPath, e);
            throw new LlmConfigException("Error loading custom configuration", e);
        }
    }

    public String getBaseUrl() {
        return config.get("baseUrl").asText();
    }
}
