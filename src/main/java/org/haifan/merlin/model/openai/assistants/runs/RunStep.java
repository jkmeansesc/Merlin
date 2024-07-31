package org.haifan.merlin.model.openai.assistants.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.LastError;
import org.haifan.merlin.model.openai.StepDetails;
import org.haifan.merlin.model.openai.Usage;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunStep {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("assistant_id")
    private String assistantId;
    @JsonProperty("thread_id")
    private String threadId;
    @JsonProperty("run_id")
    private String runId;
    private String type;
    private String status;
    @JsonProperty("step_details")
    private StepDetails stepDetails;
    @JsonProperty("last_error")
    private LastError lastError;
    @JsonProperty("expired_at")
    private Long expiredAt;
    @JsonProperty("cancelled_at")
    private Long cancelledAt;
    @JsonProperty("failed_at")
    private Long failedAt;
    @JsonProperty("completed_at")
    private Long completedAt;
    private Map<String, Object> metadata;
    private Usage usage;
}
