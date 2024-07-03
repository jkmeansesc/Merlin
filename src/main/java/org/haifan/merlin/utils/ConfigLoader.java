package org.haifan.merlin.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: add javadoc
 */
public class ConfigLoader {
    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);
    private static final String DEFAULT_CONFIG_FILE = "config/default.properties";
    private final Properties properties;

    public ConfigLoader() {
        this(DEFAULT_CONFIG_FILE);
    }

    public ConfigLoader(String configFile) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                logger.error("Config file {} not found", configFile);
                throw new IllegalArgumentException("Unable to find " + configFile);
            }
            properties.load(input);
            logger.info("Loaded config file {}", configFile);
        } catch (Exception e) {
            logger.error("Failed to load config file {}", configFile);
            throw new ConfigLoaderException("Error loading configuration", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
