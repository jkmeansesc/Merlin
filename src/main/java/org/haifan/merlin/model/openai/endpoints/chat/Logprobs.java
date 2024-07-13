package org.haifan.merlin.model.openai.endpoints.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Logprobs {
    List<MessageContentToken> content;

    @Data
    public static class MessageContentToken {

        private String token;

        private Double logprob;

        private List<Integer> bytes;

        @JsonProperty("top_logprobs")
        private List<TopLogprob> topLogprobs;

        @Data
        public static class TopLogprob {
            private String token;
            private Double logprob;
            private List<Integer> bytes;
        }
    }
}
