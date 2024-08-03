package org.haifan.merlin.model.openai.endpoints.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * <a href="https://platform.openai.com/docs/api-reference/images/createEdit">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageEditRequest {

    @NonNull
    private String prompt;

    private String model;

    private Integer n;

    private String size;

    @JsonProperty("response_format")
    private String responseFormat;

    private String user;

}
