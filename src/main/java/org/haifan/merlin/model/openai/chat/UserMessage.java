package org.haifan.merlin.model.openai.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserMessage extends Message {

    @NonNull
    private Object content;

    public UserMessage(@NotNull String content) {
        super(Fields.USER, null);
        this.content = content;
    }

    public UserMessage(@NotNull String content, String name) {
        super(Fields.USER, name);
        this.content = content;
    }

    public UserMessage(@NotNull List<ContentPart> content) {
        super(Fields.USER, null);
        this.content = content;
    }

    public UserMessage(@NotNull List<ContentPart> content, String name) {
        super(Fields.USER, name);
        this.content = content;
    }

    @JsonIgnore
    public UserMessage setContentParts(List<ContentPart> contentParts) {
        this.content = contentParts;
        return this;
    }

    @JsonIgnore
    public UserMessage addTextContent(String text) {
        addContentPart(new TextContentPart(text));
        return this;
    }

    @JsonIgnore
    public UserMessage addImageContent(String url) {
        addContentPart(new ImageContentPart(url));
        return this;
    }

    @JsonIgnore
    public UserMessage addImageContent(String url, String detail) {
        addContentPart(new ImageContentPart(url, detail));
        return this;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    private void addContentPart(ContentPart contentPart) {
        if (!(this.content instanceof List)) {
            throw new IllegalStateException("Content is not a list of content parts");
        }
        ((List<ContentPart>) this.content).add(contentPart);
    }
}
