package org.haifan.merlin.model.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    private String format;
    private String family;
    private List<String> families;
    @JsonProperty("parent_model")
    private String parentModel;
    @JsonProperty("parameter_size")
    private String parameterSize;
    @JsonProperty("quantization_level")
    private String quantizationLevel;
}
