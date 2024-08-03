package org.merlin.model.gemini.generatingcontent;

import lombok.*;
import org.merlin.model.gemini.caching.Schema;

import java.util.List;

/**
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
