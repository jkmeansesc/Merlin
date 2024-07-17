package org.haifan.merlin.model.gemini.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.Content;
import org.haifan.merlin.model.gemini.TaskType;

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

