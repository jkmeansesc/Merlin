package org.merlin.model.ollama;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaStatus {
    private String status;
    private String digest;
    private Long total;
    private Long completed;
    private String error;
}
