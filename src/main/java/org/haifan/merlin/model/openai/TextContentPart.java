package org.haifan.merlin.model.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.internal.constants.Fields;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class TextContentPart extends ContentPart {
    @NonNull
    private String text;
    private Integer index;

    public TextContentPart(@NotNull String text) {
        super(Fields.TEXT);
        this.text = text;
    }
}
