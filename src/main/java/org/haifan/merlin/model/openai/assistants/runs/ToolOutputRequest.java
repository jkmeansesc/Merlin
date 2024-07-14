package org.haifan.merlin.model.openai.assistants.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.ToolOutput;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ToolOutputRequest {
    @JsonProperty("tool_outputs")
    private List<ToolOutput> toolOutputs;
    private Boolean stream;
}
