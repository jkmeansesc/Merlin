package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/documents#resource:-document">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private String name;
    private String displayName;
    private List<CustomMetadata> customMetadata;
    private String updateTime;
    private String createTime;
}
