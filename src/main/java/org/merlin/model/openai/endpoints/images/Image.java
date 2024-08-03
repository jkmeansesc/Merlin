package org.merlin.model.openai.endpoints.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/images/object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @JsonProperty("b64_json")
    private String b64Json;

    private String url;

    @JsonProperty("revised_prompt")
    private String revisedPrompt;

}
