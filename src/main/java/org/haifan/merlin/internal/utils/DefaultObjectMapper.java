package org.haifan.merlin.internal.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.haifan.merlin.model.openai.Content;
import org.haifan.merlin.model.openai.ResponseFormat;
import org.haifan.merlin.model.openai.ToolChoice;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;

/**
 * TODO: add javadoc
 */
public class DefaultObjectMapper {

    private DefaultObjectMapper() {
        throw new IllegalStateException("Utility class");
    }

    private static ObjectMapper mapper;

    public static ObjectMapper create() {
        mapper = new ObjectMapper()
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule module = new SimpleModule();
        module.addSerializer(ResponseFormat.class, new Serializers.ResponseFormatSerializer());
        module.addDeserializer(ResponseFormat.class, new Serializers.ResponseFormatDeserializer());

        module.addSerializer(ToolChoice.class, new Serializers.ToolChoiceSerializer());
        module.addDeserializer(ToolChoice.class, new Serializers.ToolChoiceDeserializer());

        module.addSerializer(EmbeddingRequest.Input.class, new Serializers.InputSerializer());
        module.addDeserializer(EmbeddingRequest.Input.class, new Serializers.InputDeserializer());

        module.addSerializer(Content.class, new Serializers.ContentSerializer());
        module.addDeserializer(Content.class, new Serializers.ContentDeserializer());

        mapper.registerModule(module);
        return mapper;
    }

    public static ObjectMapper get() {
        return mapper;
    }
}
