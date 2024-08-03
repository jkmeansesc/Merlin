package org.merlin.model.openai.endpoints.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/createTranscription">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptionRequest {
    @NonNull
    private String model;

    private String language;

    private String prompt;

    @JsonProperty("response_format")
    private String responseFormat;

    private Double temperature;

    @JsonProperty("timestamp_granularities")
    private Long[] timestampGranularities;
}
