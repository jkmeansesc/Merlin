package org.haifan.merlin.model.openai.endpoints.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The request object for creating transcription from audio.
 * <a href="https://platform.openai.com/docs/api-reference/audio/createTranscription">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
