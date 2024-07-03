package org.haifan.merlin.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * TODO: add javadoc
 */
public class ConfigLoader {
    private static final String DEFAULT_CONFIG_FILE = "config/default.properties";
    private final Properties properties;

    public ConfigLoader() {
        this(DEFAULT_CONFIG_FILE);
    }

    public ConfigLoader(String configFile) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new IllegalArgumentException("Unable to find " + configFile);
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
