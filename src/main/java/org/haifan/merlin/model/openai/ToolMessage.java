package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;


/**
 * TODO: add javadoc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ToolMessage extends Message {
    @NonNull
    private String content;

    @NonNull
    @JsonProperty("tool_call_id")
    private String toolCallId;

    public ToolMessage(@NotNull String content, @NotNull String toolCallId) {
        super(Fields.TOOL, null);
        this.content = content;
        this.toolCallId = toolCallId;
    }
}

