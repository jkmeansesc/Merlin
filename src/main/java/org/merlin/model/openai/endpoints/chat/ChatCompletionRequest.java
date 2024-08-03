package org.merlin.model.openai.endpoints.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.merlin.model.openai.OpenAiMessage;
import org.merlin.model.openai.ResponseFormat;
import org.merlin.model.openai.Tool;
import org.merlin.model.openai.ToolChoice;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://platform.openai.com/docs/api-reference/chat/create">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {

    @NonNull
    private List<OpenAiMessage> messages;

    @NonNull
    private String model;

    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map<String, Integer> logitBias;

    private Boolean logprobs;

    @JsonProperty("top_logprobs")
    private Integer topLogprobs;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Integer n;

    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    private Integer seed;

    @JsonProperty("service_tier")
    private String serviceTier;

    private List<String> stop;

    private Boolean stream;

    @JsonProperty("stream_options")
    private StreamOptions streamOptions;

    private Double temperature;

    @JsonProperty("top_p")
    private Double topP;

    private List<Tool> tools;

    @JsonProperty("tool_choice")
    private ToolChoice toolChoice;

    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;

    private String user;

    @Data
    public static class StreamOptions {
        @JsonProperty("include_usage")
        private Boolean includeUsage;
    }
}
