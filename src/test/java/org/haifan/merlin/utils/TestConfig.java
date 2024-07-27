package org.haifan.merlin.utils;

public class TestConfig {
    private TestConfig() {
        throw new IllegalStateException("Utility class");
    }

    private static final String USE_MOCK_PROPERTY = "merlin.test.useMock";

    public static boolean useMock() {
        return Boolean.parseBoolean(System.getProperty(USE_MOCK_PROPERTY, "true"));
    }
}
