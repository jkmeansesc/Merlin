package org.haifan.merlin.model.gemini;

import lombok.Data;

import java.util.List;

/**
 * Response from the model supporting multiple candidates.
 * <a href="https://ai.google.dev/api/rest/v1/GenerateContentResponse">...</a>
 */
@Data
public class GenerateContentResponse {
    private List<Candidate> candidates;
    private PromptFeedback promptFeedback;
    private UsageMetadata usageMetadata;

    @Data
    public static class Candidate {
        private Content content;
        private FinishReason finishReason;
        private List<SafetyRating> safetyRatings;
        private CitationMetadata citationMetadata;
        private Integer tokenCount;
        private Integer index;

        public enum FinishReason {
            FINISH_REASON_UNSPECIFIED,
            STOP,
            MAX_TOKENS,
            SAFETY,
            RECITATION,
            OTHER
        }


        @Data
        public static class CitationMetadata {
            private List<CitationSource> citationSources;

            @Data
            public static class CitationSource {
                private Integer startIndex;
                private Integer endIndex;
                private String uri;
                private String license;
            }
        }
    }

    @Data
    public static class PromptFeedback {
        private BlockReason blockReason;
        private List<SafetyRating> safetyRatings;

        public enum BlockReason {
            BLOCK_REASON_UNSPECIFIED,
            SAFETY,
            OTHER
        }
    }

    @Data
    public static class UsageMetadata {
        private Integer promptTokenCount;
        private Integer candidatesTokenCount;
        private Integer totalTokenCount;
    }

    @Data
    public static class SafetyRating {
        private HarmCategory category;
        private HarmProbability probability;
        private Boolean blocked;

        public enum HarmProbability {
            HARM_PROBABILITY_UNSPECIFIED,
            NEGLIGIBLE,
            LOW,
            MEDIUM,
            HIGH
        }
    }
}
