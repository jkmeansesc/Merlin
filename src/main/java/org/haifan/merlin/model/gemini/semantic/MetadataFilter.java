package org.haifan.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/corpora#metadatafilter">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataFilter {
    private String key;
    List<Condition> conditions;
}
