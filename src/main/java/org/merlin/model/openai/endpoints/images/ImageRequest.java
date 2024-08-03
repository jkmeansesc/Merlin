package org.merlin.model.openai.endpoints.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * <a href="https://platform.openai.com/docs/api-reference/images">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {
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
