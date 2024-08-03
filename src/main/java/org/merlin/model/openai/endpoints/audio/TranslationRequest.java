package org.merlin.model.openai.endpoints.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/createTranslation">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslationRequest {

    @NonNull
    private String model;

    private String prompt;

    @JsonProperty("response_format")
    private String responseFormat;

    private Double temperature;

}
