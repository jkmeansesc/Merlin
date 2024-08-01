package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.*;
import org.haifan.merlin.model.gemini.caching.Schema;

import java.util.List;

/**
 * Configuration options for model generation and outputs. Not all parameters may be configurable for every model.
 * <a href="https://ai.google.dev/api/rest/v1/GenerationConfig">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationConfig {
    private List<String> stopSequences;
    private String responseMimeType;
    private Schema responseSchema;
    private Integer candidateCount;
    private Integer maxOutputTokens;
    private Double temperature;
    private Double topP;
    private Double topK;
}
