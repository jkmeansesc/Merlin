package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAiMessage {
    public enum Role {
        SYSTEM, USER, ASSISTANT, TOOL
    }
    private Role role;
    private String name;
    private String content;
    @JsonProperty("tool_call_id")
    private String toolCallId;
    @JsonProperty("tool_calls")
    private List<ToolCall> toolCalls;

    public OpenAiMessage(@NotNull Role role, String content) {
        this.role = role;
        this.content = content;
    }

    public OpenAiMessage(@NotNull Role role, String content, String toolCallId) {
        this.role = role;
        this.content = content;
        this.toolCallId = toolCallId;
    }

    @JsonProperty("role")
    public String getRoleAsString() {
        return role != null ? role.name().toLowerCase() : null;
    }

    @JsonSetter("role")
    public void setRoleFromString(String role) {
        try {
            this.role = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.role = null;
        }
    }
}
