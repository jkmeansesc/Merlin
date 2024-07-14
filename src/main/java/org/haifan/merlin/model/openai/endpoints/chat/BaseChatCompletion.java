package org.haifan.merlin.model.openai.endpoints.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.Logprobs;
import org.haifan.merlin.model.openai.Message;
import org.haifan.merlin.model.openai.Usage;

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

        @JsonAlias(Fields.DELTA)
        private Message message;

        private Logprobs logprobs;

        @JsonProperty("finish_reason")
        private String finishReason;

        private Integer index;
    }
}
