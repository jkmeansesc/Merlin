package org.haifan.merlin.internal.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.haifan.merlin.model.openai.*;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Serializers {
    private Serializers() {
        throw new IllegalStateException("Utility class");
    }

    public static class ResponseFormatSerializer extends JsonSerializer<ResponseFormat> {
        @Override
        public void serialize(ResponseFormat value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if ("auto".equals(value.getType())) {
                gen.writeString("auto");
            } else {
                gen.writeStartObject();
                gen.writeStringField("type", value.getType());
                gen.writeEndObject();
            }
        }
    }

    public static class ResponseFormatDeserializer extends JsonDeserializer<ResponseFormat> {
        @Override
        public ResponseFormat deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.getCurrentToken().isScalarValue()) {
                String value = p.getValueAsString();
                if ("auto".equals(value)) {
                    return new ResponseFormat("auto");
                }
            } else if (p.getCurrentToken().isStructStart()) {
                p.nextToken(); // Move to the "type" field
                if ("type".equals(p.currentName())) {
                    p.nextToken(); // Move to the value of "type"
                    return new ResponseFormat(p.getValueAsString());
                }
            }
            throw new IOException("Invalid ResponseFormat");
        }
    }

    public static class ToolChoiceSerializer extends JsonSerializer<ToolChoice> {
        @Override
        public void serialize(ToolChoice toolChoice, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (toolChoice.getType() != null) {
                gen.writeString(toolChoice.getType());
            } else if (toolChoice.getTool() != null) {
                gen.writeObject(toolChoice.getTool());
            } else {
                gen.writeNull();
            }
        }
    }

    public static class ToolChoiceDeserializer extends JsonDeserializer<ToolChoice> {
        @Override
        public ToolChoice deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                return new ToolChoice(p.getText());
            } else if (p.getCurrentToken() == JsonToken.START_OBJECT) {
                Tool tool = p.readValueAs(Tool.class);
                return new ToolChoice(tool);
            }
            return null;
        }
    }

    public static class InputSerializer extends JsonSerializer<EmbeddingRequest.Input> {
        @Override
        public void serialize(EmbeddingRequest.Input value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value.getInputStr() != null) {
                gen.writeString(value.getInputStr());
            } else if (value.getInputList() != null) {
                gen.writeObject(value.getInputList());
            } else {
                gen.writeNull();
            }
        }
    }

    public static class InputDeserializer extends JsonDeserializer<EmbeddingRequest.Input> {
        @Override
        public EmbeddingRequest.Input deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            List<Object> list;
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                return new EmbeddingRequest.Input(p.getText());
            } else if (p.currentToken() == JsonToken.START_ARRAY) {
                list = new ArrayList<>();
                while (p.nextToken() != JsonToken.END_ARRAY) {
                    if (p.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                        list.add(p.getIntValue());
                    } else if (p.currentToken() == JsonToken.VALUE_STRING) {
                        list.add(p.getText());
                    } else {
                        throw new IOException("Unexpected token in array: " + p.currentToken());
                    }
                }
                return new EmbeddingRequest.Input(list);
            }
            throw new IOException("Invalid Input format");
        }
    }

    public static class ContentSerializer extends JsonSerializer<Content> {
        @Override
        public void serialize(Content value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value.getContentStr() != null) {
                gen.writeString(value.getContentStr());
            } else if (value.getContentParts() != null && !value.getContentParts().isEmpty()) {
                gen.writeObject(value.getContentParts());
            } else {
                gen.writeNull();
            }
        }
    }

    public static class ContentDeserializer extends JsonDeserializer<Content> {
        @Override
        public Content deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            List<ContentPart> parts;
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                return new Content(p.getText());
            } else if (p.currentToken() == JsonToken.START_ARRAY) {
                parts = ctxt.readValue(p, ctxt.getTypeFactory().constructCollectionType(List.class, ContentPart.class));
                return new Content(parts);
            }
            throw new IOException("Invalid Content format");
        }
    }
}
