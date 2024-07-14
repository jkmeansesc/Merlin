package org.haifan.merlin.model.openai;

import lombok.*;

@Data
public class Tool {
    @NonNull
    private String type;

    @NonNull
    private Function function;
}
