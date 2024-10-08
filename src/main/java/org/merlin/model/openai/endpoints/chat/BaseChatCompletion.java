package org.merlin.model.openai.endpoints.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.internal.constants.Fields;
import org.merlin.model.openai.Logprobs;
import org.merlin.model.openai.OpenAiMessage;
import org.merlin.model.openai.Usage;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

        @JsonAlias(Fields.DELTA)
        private OpenAiMessage message;

        private Logprobs logprobs;

        @JsonProperty("finish_reason")
        private String finishReason;

        private Integer index;
    }
}
