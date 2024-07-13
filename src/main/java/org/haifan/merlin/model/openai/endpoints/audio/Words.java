package org.haifan.merlin.model.openai.endpoints.audio;

import lombok.Data;

/**
 * The words object for extracted words and their corresponding timestamps.
 * <a href="https://platform.openai.com/docs/api-reference/audio/verbose-json-object#audio/verbose-json-object-words">...</a>
 */
@Data
public class Words {
    private String word;
    private double start;
    private double end;
}
