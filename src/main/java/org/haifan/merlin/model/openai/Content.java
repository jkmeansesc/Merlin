package org.haifan.merlin.model.openai;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Content {
    private String contentStr;
    private List<ContentPart> contentParts = new ArrayList<>();

    public Content(String content) {
        this.contentStr = content;
    }

    public Content(List<ContentPart> contentParts) {
        this.contentParts = contentParts;
    }
}
