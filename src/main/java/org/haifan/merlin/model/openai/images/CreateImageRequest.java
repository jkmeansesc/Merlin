package org.haifan.merlin.model.openai.images;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The request object to create an image from OpenAI.
 * <a href="https://platform.openai.com/docs/api-reference/images">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateImageRequest {
    @NonNull
    private String prompt;

    private String model;

    private Integer n;

    private String quality;

    @JsonProperty("response_format")
    private String responseFormat;

    private String size;

    private String style;

    private String user;
}
