package org.haifan.merlin.model.openai.assistants.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.haifan.merlin.model.openai.*;
import org.haifan.merlin.model.openai.assistants.threads.ThreadRequest;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RunRequest {
    @JsonProperty("assistant_id")
    private String assistantId;
    private String model;
    private String instructions;
    @JsonProperty("additional_instructions")
    private String additionalInstructions;
    @JsonProperty("additional_messages")
    private List<OpenAiMessage> additionalMessages;
    private List<Tool> tools;
    private Map<String,Object> metadata;
    private Double temperature;
    @JsonProperty("top_p")
    private Double topP;
    private Boolean stream;
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
    private ThreadRequest thread;
}
