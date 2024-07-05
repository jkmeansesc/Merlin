package org.haifan.merlin.model.openai.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Image {

    @JsonProperty("b64_json")
    private String b64Json;

    private String url;

    @JsonProperty("revised_prompt")
    private String revisedPrompt;

}
