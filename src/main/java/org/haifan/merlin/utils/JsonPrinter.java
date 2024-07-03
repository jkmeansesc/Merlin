package org.haifan.merlin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class for printing Java objects as JSON strings.
 */
public class JsonPrinter {

    private JsonPrinter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Prints the given object as a formatted JSON string.
     *
     * @param obj the object to serialize to JSON
     * @return a formatted JSON string
     * @throws JsonPrinterException if there is an error during JSON serialization
     */
    public static String print(Object obj) throws JsonPrinterException {
        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonPrinterException("Error printing object as JSON", e);
        }
    }
}
