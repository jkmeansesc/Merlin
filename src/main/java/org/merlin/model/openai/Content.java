package org.merlin.model.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private String contentStr;
    private List<ContentPart> contentParts;

    public Content(String content) {
        this.contentStr = content;
    }

    public Content(List<ContentPart> contentParts) {
        this.contentParts = contentParts;
    }
}
