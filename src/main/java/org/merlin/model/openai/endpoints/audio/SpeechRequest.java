package org.merlin.model.openai.endpoints.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/createSpeech">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechRequest {

    @NonNull
    private String model;

    @NonNull
    private String input;

    @NonNull
    private String voice;

    @JsonProperty("response_format")
    private String responseFormat;

    private Double speed;
}
