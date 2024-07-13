package org.haifan.merlin.model.openai.endpoints.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageVariationRequest {

    private String model;

    private Integer n;

    @JsonProperty("response_format")
    private String responseFormat;

    private String size;

    private String user;
}
