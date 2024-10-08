package org.merlin.internal.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import org.merlin.internal.constants.Fields;
import org.merlin.model.openai.*;
import org.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;

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
                gen.writeStartArray();
                for (ContentPart part : value.getContentParts()) {
                    serializers.defaultSerializeValue(part, gen);
                }
                gen.writeEndArray();
            } else {
                gen.writeNull();
            }
        }
    }

    public static class ContentDeserializer extends JsonDeserializer<Content> {
        @Override
        public Content deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                return new Content(p.getText());
            } else if (p.currentToken() == JsonToken.START_ARRAY) {
                List<ContentPart> parts = ctxt.readValue(p, ctxt.getTypeFactory().constructCollectionType(List.class, ContentPart.class));
                return new Content(parts);
            }
            throw new IOException("Invalid Content format");
        }
    }

    public static class TextSerializer extends JsonSerializer<Text> {
        @Override
        public void serialize(Text value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value.getValue() != null) {
                gen.writeString(value.getValue());
            } else {
                gen.writeNull();
            }
        }
    }

    public static class TextDeserializer extends JsonDeserializer<Text> {
        @Override
        public Text deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                Text text = new Text();
                text.setValue(p.getText());
                return text;
            } else if (p.currentToken() == JsonToken.START_OBJECT) {
                ObjectCodec codec = p.getCodec();
                JsonNode node = codec.readTree(p);

                String value = node.has(Fields.VALUE) ? node.get(Fields.VALUE).asText() : null;

                List<Annotation> annotations = new ArrayList<>();
                if (node.has(Fields.ANNOTATIONS) && node.get(Fields.ANNOTATIONS).isArray()) {
                    JsonNode annotationsNode = node.get(Fields.ANNOTATIONS);
                    for (JsonNode annotationNode : annotationsNode) {
                        Annotation annotation = codec.treeToValue(annotationNode, Annotation.class);
                        annotations.add(annotation);
                    }
                }

                return new Text(value, annotations);
            }
            throw new IOException("Invalid text format");
        }
    }
}
