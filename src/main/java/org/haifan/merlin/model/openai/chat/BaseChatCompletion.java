package org.haifan.merlin.model.openai.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.constants.Fields;

import java.util.List;

@Data
public class BaseChatCompletion {

    private String id;

    private List<Choice> choices;

    private Long created;

    private String model;

    @JsonProperty("service_tier")
    private String serviceTier;

    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    private String object;

    private Usage usage;

    @Data
    public static class Choice {

        @JsonProperty(Fields.DELTA)
        @JsonAlias(Fields.MESSAGE)
        private ChatMessage content;

        private Logprobs logprobs;

        @JsonProperty("finish_reason")
        private String finishReason;

        private Integer index;
    }

    @Data
    public static class Usage {

        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
