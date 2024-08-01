package org.haifan.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/documents#custommetadata">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMetadata {
    private String key;
    private String stringValue;
    private StringList stringListValue;
    private Double numericValue;
}
