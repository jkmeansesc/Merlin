package org.haifan.merlin.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.haifan.merlin.model.openai.ResponseFormat;
import org.haifan.merlin.model.openai.endpoints.chat.ToolChoice;

/**
 * TODO: add javadoc
 */
public class DefaultObjectMapper {

    private DefaultObjectMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule module = new SimpleModule();
        module.addSerializer(ResponseFormat.class, new Serializers.ResponseFormatSerializer());
        module.addDeserializer(ResponseFormat.class, new Serializers.ResponseFormatDeserializer());

        module.addSerializer(ToolChoice.class, new Serializers.ToolChoiceSerializer());
        module.addDeserializer(ToolChoice.class, new Serializers.ToolChoiceDeserializer());

        mapper.registerModule(module);
        return mapper;
    }
}
