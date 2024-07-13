package org.haifan.merlin.model.openai.endpoints.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The request object to create translation from an audio file for OpenAI
 * <a href="https://platform.openai.com/docs/api-reference/audio/createTranslation">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TranslationRequest {

    @NonNull
    private String model;

    private String prompt;

    @JsonProperty("response_format")
    private String responseFormat;

    private Double temperature;

}
