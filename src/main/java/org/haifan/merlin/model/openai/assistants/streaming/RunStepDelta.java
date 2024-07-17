package org.haifan.merlin.model.openai.assistants.streaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.model.openai.StepDetails;

@Data
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
