package org.haifan.merlin.model.gemini.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class Model {
    @NonNull
    private String name;
    @NonNull
    private String baseModelId;
    @NonNull
    private String version;
    private String displayName;
    private String description;
    private Integer inputTokenLimit;
    private Integer outputTokenLimit;
    private List<String> supportedGenerationMethods;
    private Double temperature;
    private Double topP;
    private Integer topK;
}
