package org.haifan.merlin.model.openai.endpoints;

import lombok.Data;
import lombok.NonNull;

import java.util.Map;

@Data
public class Function {
    private String description;
    @NonNull
    private String name;
    private Map<String, Object> parameters;
}
