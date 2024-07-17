package org.haifan.merlin.model.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmbedContentRequest {
    private Content content;
    private TaskType taskType;
    private String title;
    private Integer outputDimensionality;
}

