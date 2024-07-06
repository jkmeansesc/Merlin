package org.haifan.merlin.model.openai.audio;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CreateTranscriptionRequest {
    @NonNull
    String model;

    String language;

    String prompt;

    @JsonProperty("response_format")
    String responseFormat;

    Double temperature;

}
