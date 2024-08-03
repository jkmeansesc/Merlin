package org.merlin.model.openai.assistants.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.merlin.model.openai.ToolOutput;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolOutputRequest {
    @JsonProperty("tool_outputs")
    private List<ToolOutput> toolOutputs;
    private Boolean stream;
}
