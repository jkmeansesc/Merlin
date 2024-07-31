package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.model.openai.assistants.runs.Run;

import java.util.List;

@Data
public class RequiredAction {
    private String type;
    @JsonProperty("submit_tool_outputs")
    private SubmitToolOutputs submitToolOutputs;

    @Data
    public static class SubmitToolOutputs {
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;
    }
}
