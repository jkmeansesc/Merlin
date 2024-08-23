package org.merlin.internal.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.merlin.model.openai.Content;
import org.merlin.model.openai.ResponseFormat;
import org.merlin.model.openai.Text;
import org.merlin.model.openai.ToolChoice;
import org.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;

/**
 * A utility class for creating and managing a singleton instance of {@link ObjectMapper}.
 * This class provides methods to configure the {@link ObjectMapper} with custom serializers
 * and deserializers.
 */
public class DefaultObjectMapper {

    private DefaultObjectMapper() {
        throw new IllegalStateException("Utility class");
    }

    private static ObjectMapper mapper;

    /**
     * Creates and configures a new instance of {@link ObjectMapper} with custom
     * serializers and deserializers for the following classes:
     * <ul>
     * <li>{@link ResponseFormat}</li>
     * <li>{@link ToolChoice}</li>
     * <li>{@link EmbeddingRequest.Input}</li>
     * <li>{@link Content}</li>
     * <li>{@link Text}</li>
     * </ul>
     * This method ensures that the {@link ObjectMapper} includes non-null values during serialization.
     *
     * @return a configured {@link ObjectMapper} instance.
     */
    public static ObjectMapper create() {
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
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

        module.addSerializer(Text.class, new Serializers.TextSerializer());
        module.addDeserializer(Text.class, new Serializers.TextDeserializer());

        mapper.registerModule(module);
        return mapper;
    }

    /**
     * Returns the singleton instance of {@link ObjectMapper}. If the instance is not yet created,
     * it calls the {@link #create()} method to create and configure it.
     *
     * @return the singleton {@link ObjectMapper} instance.
     */
    public static ObjectMapper get() {
        if (mapper == null) {
            return create();
        }
        return mapper;
    }

    /**
     * Prints the given object as a formatted JSON string.
     *
     * @param obj the object to serialize to JSON
     * @return a formatted JSON string
     * @throws DefaultObjectMapperException if there is an error during JSON serialization
     */
    public static String print(Object obj) {
        if (mapper == null) {
            create();
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new DefaultObjectMapperException("Error serializing object as JSON", e);
        }
    }
}
