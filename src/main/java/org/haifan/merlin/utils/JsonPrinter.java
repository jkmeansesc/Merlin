package org.haifan.merlin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (obj instanceof String) {
                // If it's already a string, parse and pretty print it
                Object json = mapper.readValue((String) obj, Object.class);
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            } else {
                // If it's an object, pretty print it
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            throw new JsonPrinterException("Error printing object as JSON", e);
        }
    }
}
