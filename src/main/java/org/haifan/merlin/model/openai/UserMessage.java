package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserMessage extends Message {

    @NonNull
    private Content content;

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
        this.content.getContentParts().add(new TextContentPart(text));
        return this;
    }

    @JsonIgnore
    public UserMessage addImageUrlContent(String url) {
        this.content.getContentParts().add(new ImageUrlContentPart(url));
        return this;
    }

    @JsonIgnore
    public UserMessage addImageUrlContent(String url, String detail) {
        this.content.getContentParts().add(new ImageUrlContentPart(url, detail));
        return this;
    }

    /**
     * BETA: only for Assistants-Threads endpoints
     */
    @JsonIgnore
    public UserMessage addImageFileContent(String url) {
        this.content.getContentParts().add(new ImageFileContentPart(url));
        return this;
    }

    /**
     * BETA: only for Assistants-Threads endpoints
     */
    @JsonIgnore
    public UserMessage addImageFileContent(String url, String detail) {
        this.content.getContentParts().add(new ImageFileContentPart(url, detail));
        return this;
    }
}
