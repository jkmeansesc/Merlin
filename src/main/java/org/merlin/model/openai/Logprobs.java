package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
