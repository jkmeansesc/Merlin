package org.haifan.merlin.model.openai.chat;

import lombok.*;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SystemMessage extends Message {
    @NonNull
    private String content;

    public SystemMessage(@NotNull String content) {
        super(Fields.SYSTEM, null);
        this.content = content;
    }

    public SystemMessage(@NotNull String content, String name) {
        super(Fields.SYSTEM, name);
        this.content = content;
    }
}
