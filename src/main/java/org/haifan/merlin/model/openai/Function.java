package org.haifan.merlin.model.openai;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Function {
    @NonNull
    private String name;
    private String description;
    private Map<String, Object> parameters;
}
