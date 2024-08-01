package org.haifan.merlin.model.gemini.embeddings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.caching.Content;

/**
 * <a href="https://ai.google.dev/api/embeddings#embedcontentrequest">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbedContentRequest {
    private String model;
    private Content content;
    private TaskType taskType;
    private String title;
    private Integer outputDimensionality;
}

