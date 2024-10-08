package org.merlin.model.gemini.models;

import lombok.*;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/models#resource:-model">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer maxTemperature;
}
