package org.haifan.merlin.model.openai.images;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The request object to create an edited or extended image from OpenAI.
 * <a href="https://platform.openai.com/docs/api-reference/images/createEdit">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateImageEditRequest {

    @NonNull
    private String prompt;

    private String model;

    private Integer n;

    private String size;

    @JsonProperty("response_format")
    private String responseFormat;

    private String user;

}
