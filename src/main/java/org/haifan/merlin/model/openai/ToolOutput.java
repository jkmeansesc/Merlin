package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolOutput {
    @JsonProperty("tool_call_id")
    private String toolCallId;
    private String output;
}
