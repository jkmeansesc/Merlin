package org.merlin.model.openai.assistants.streaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.openai.StepDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunStepDelta {
    private String id;
    private String object;
    private Delta delta;

    @Data
    public static class Delta {
        @JsonProperty("step_details")
        private StepDetails stepDetails;
    }
}
