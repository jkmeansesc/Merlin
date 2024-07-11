package org.haifan.merlin.model.openai.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The request object to generate audio from OpenAI.
 * <a href="https://platform.openai.com/docs/api-reference/audio/createSpeech">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
