package org.merlin.model.openai.assistants.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.merlin.model.openai.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Run {

    private String id;
    private String object;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("thread_id")
    private String threadId;
    @JsonProperty("assistant_id")
    private String assistantId;
    private String status;
    @JsonProperty("required_action")
    private RequiredAction requiredAction;
    @JsonProperty("last_error")
    private LastError lastError;
    @JsonProperty("expires_at")
    private Long expiresAt;
    @JsonProperty("started_at")
    private Long startedAt;
    @JsonProperty("cancelled_at")
    private Long cancelledAt;
    @JsonProperty("failed_at")
    private Long failedAt;
    @JsonProperty("completed_at")
    private Long completedAt;
    @JsonProperty("incomplete_details")
    private IncompleteDetails incompleteDetails;
    private String model;
    private String instructions;
    private List<Tool> tools;
    private Map<String, Object> metadata;
    private Usage usage;
    private Double temperature;
    @JsonProperty("top_p")
    private Double topP;
    @JsonProperty("max_prompt_tokens")
    private Integer maxPromptTokens;
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;
    @JsonProperty("truncation_strategy")
    private TruncationStrategy truncationStrategy;
    @JsonProperty("tool_choice")
    private ToolChoice toolChoice;
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;
    // not documented by openai but present in its examples
    // see https://platform.openai.com/docs/api-reference/runs/listRuns
    @JsonProperty("tool_resources")
    private ToolResources toolResources;

}
