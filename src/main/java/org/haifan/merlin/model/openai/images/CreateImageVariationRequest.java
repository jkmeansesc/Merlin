package org.haifan.merlin.model.openai.images;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateImageVariationRequest {

    private String model;

    private Integer n;

    @JsonProperty("response_format")
    private String responseFormat;

    private String size;

    private String user;
}
