package org.haifan.merlin.model.openai.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message {
    @NonNull
    protected String role;
    protected String name;
}
