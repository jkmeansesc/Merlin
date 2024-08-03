package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.merlin.internal.constants.Fields;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class StepDetailsToolCalls extends StepDetails {
    @JsonProperty("tool_calls")
    private List<ToolCall> toolCallList;

    public StepDetailsToolCalls() {
        super(Fields.TOOL_CALLS);
    }
}
