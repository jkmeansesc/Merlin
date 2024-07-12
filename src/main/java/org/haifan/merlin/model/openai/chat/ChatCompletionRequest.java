package org.haifan.merlin.model.openai.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatCompletionRequest {

    @NonNull
    private List<Message> messages;

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
    public static class ResponseFormat {
        private String type;
    }

    @Data
    public static class StreamOptions {
        @JsonProperty("include_usage")
        private Boolean includeUsage;
    }
}
