package org.haifan.merlin.model.openai.endpoints.chat;

import lombok.*;
import org.haifan.merlin.model.openai.endpoints.Function;

@Data
public class Tool {
    @NonNull
    private String type;

    @NonNull
    private Function function;
}
