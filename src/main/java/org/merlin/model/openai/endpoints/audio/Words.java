package org.merlin.model.openai.endpoints.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/verbose-json-object#audio/verbose-json-object-words">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Words {
    private String word;
    private double start;
    private double end;
}
