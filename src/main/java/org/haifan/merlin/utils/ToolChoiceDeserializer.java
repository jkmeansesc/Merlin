package org.haifan.merlin.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.haifan.merlin.model.openai.chat.Tool;
import org.haifan.merlin.model.openai.chat.ToolChoice;

import java.io.IOException;

public class ToolChoiceDeserializer extends JsonDeserializer<ToolChoice> {

    @Override
    public ToolChoice deserialize(JsonParser p, DeserializationContext context) throws IOException {
        ObjectNode node = p.getCodec().readTree(p);
        if (node.isTextual()) {
            return new ToolChoice(node.asText());
        } else {
            return new ToolChoice(p.getCodec().treeToValue(node, Tool.class));
        }
    }
}
