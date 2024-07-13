package org.haifan.merlin.model.openai.assistants;

import lombok.Data;
import lombok.NonNull;

@Data
public class ExpiresAfter {
    @NonNull
    private String anchor;
    @NonNull
    private Integer days;
}
