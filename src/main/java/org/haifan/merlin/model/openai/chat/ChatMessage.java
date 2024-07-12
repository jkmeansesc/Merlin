package org.haifan.merlin.model.openai.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatMessage {

    @JsonInclude() // content should always exist in the call, even if it is null
    private String content;

    @JsonProperty("tool_calls")
    private List<ToolCall> toolCalls;

    private String role;
}
