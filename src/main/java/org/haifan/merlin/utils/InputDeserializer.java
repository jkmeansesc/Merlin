package org.haifan.merlin.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: add javadoc
 */
public class InputDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isArray()) {
            return deserializeArray(node);
        }
        return null;
    }

    private List<Object> deserializeArray(JsonNode arrayNode) {
        List<Object> list = new ArrayList<>();
        for (JsonNode element : arrayNode) {
            list.add(deserializeArrayElement(element));
        }
        return list;
    }

    private Object deserializeArrayElement(JsonNode element) {
        if (element.isTextual()) {
            return element.asText();
        } else if (element.isNumber()) {
            return element.asInt();
        } else if (element.isArray()) {
            return deserializeIntegerArray(element);
        }
        return null;
    }

    private List<Integer> deserializeIntegerArray(JsonNode arrayNode) {
        List<Integer> innerList = new ArrayList<>();
        for (JsonNode innerElement : arrayNode) {
            if (innerElement.isNumber()) {
                innerList.add(innerElement.asInt());
            }
        }
        return innerList;
    }
}
