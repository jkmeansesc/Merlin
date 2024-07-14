package org.haifan.merlin.model.openai.endpoints.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserMessage extends Message {

    @NonNull
    private Content content;

    @Data
    @NoArgsConstructor
    public static class Content {
        private String contentStr;
        private List<ContentPart> contentParts = new ArrayList<>();

        public Content(String content) {
            this.contentStr = content;
        }

        public Content(List<ContentPart> contentParts) {
            this.contentParts = contentParts;
        }
    }

    public UserMessage() {
        super(Fields.USER, null);
        this.content = new Content();
    }

    public UserMessage(@NotNull String content) {
        super(Fields.USER, null);
        this.content = new Content(content);
    }

    public UserMessage(@NotNull String content, String name) {
        super(Fields.USER, name);
        this.content = new Content(content);
    }

    public UserMessage(@NotNull List<ContentPart> content) {
        super(Fields.USER, null);
        this.content = new Content(content);
    }

    public UserMessage(@NotNull List<ContentPart> content, String name) {
        super(Fields.USER, name);
        this.content = new Content(content);
    }

    @JsonIgnore
    public UserMessage setContentParts(List<ContentPart> contentParts) {
        this.content = new Content(contentParts);
        return this;
    }

    @JsonIgnore
    public UserMessage addTextContent(String text) {
        this.content.contentParts.add(new TextContentPart(text));
        return this;
    }

    @JsonIgnore
    public UserMessage addImageContent(String url) {
        this.content.contentParts.add(new ImageContentPart(url));
        return this;
    }

    @JsonIgnore
    public UserMessage addImageContent(String url, String detail) {
        this.content.contentParts.add(new ImageContentPart(url, detail));
        return this;
    }
}
