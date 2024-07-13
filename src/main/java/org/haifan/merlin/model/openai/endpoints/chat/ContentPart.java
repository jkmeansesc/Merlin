package org.haifan.merlin.model.openai.endpoints.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ContentPart {
    @NonNull
    private String type;
}
