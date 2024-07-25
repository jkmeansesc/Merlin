package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.internal.constants.Fields;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StepDetailsToolCalls extends StepDetails{
    @JsonProperty("tool_calls")
    private List<ToolCall> toolCallList;

    public StepDetailsToolCalls() {
        super(Fields.TOOL_CALLS);
    }
}
