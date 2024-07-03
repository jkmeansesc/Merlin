package org.haifan.merlin.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * A helper method to print the json in a readable format.
 */
public class JsonPrinter {

    private JsonPrinter() {
        throw new IllegalStateException("Utility class");
    }

    public static void print(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = mapper.writeValueAsString(obj);
        System.out.println(json);
    }
}
