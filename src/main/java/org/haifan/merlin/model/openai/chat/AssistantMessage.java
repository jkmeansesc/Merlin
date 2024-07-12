package org.haifan.merlin.model.openai.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.constants.Fields;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssistantMessage extends Message {
    private String content;

    @JsonProperty("tool_calls")
    private List<ToolCall> toolCalls;

    public AssistantMessage() {
        super(Fields.ASSISTANT, null);
    }

    public AssistantMessage(String content) {
        super(Fields.ASSISTANT, null);
        this.content = content;
    }

    public AssistantMessage(String content, String name) {
        super(Fields.ASSISTANT, name);
        this.content = content;
    }

    public AssistantMessage(String content, String name, List<ToolCall> toolCalls) {
        super(Fields.ASSISTANT, name);
        this.content = content;
        this.toolCalls = toolCalls;
    }

    public AssistantMessage(List<ToolCall> toolCalls) {
        super(Fields.ASSISTANT, null);
        this.toolCalls = toolCalls;
    }

    @JsonIgnore
    public AssistantMessage addToolCall(ToolCall toolCall) {
        this.toolCalls.add(toolCall);
        return this;
    }
}
