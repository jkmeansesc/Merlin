package org.haifan.merlin.model.openai;

import lombok.Data;

import java.util.List;

@Data
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
