package org.haifan.merlin.model.openai.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class TextContentPart extends ContentPart {
    @NonNull
    private String text;

    public TextContentPart(@NotNull String text) {
        super(Fields.TEXT);
        this.text = text;
    }
}
