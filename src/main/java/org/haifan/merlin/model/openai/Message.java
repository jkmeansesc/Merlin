package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Abstract class representing a message with role-based polymorphic behavior.
 * This class is used as a base for different types of messages in the system.
 * The type of message is determined by the {@code role} property.
 * <a href="https://platform.openai.com/docs/api-reference/chat/create#chat-create-messages">...</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "role"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SystemMessage.class, name = "system"),
        @JsonSubTypes.Type(value = UserMessage.class, name = "user"),
        @JsonSubTypes.Type(value = AssistantMessage.class, name = "assistant"),
        @JsonSubTypes.Type(value = ToolMessage.class, name = "tool")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message {
    @NonNull
    protected String role;
    protected String name;
}
