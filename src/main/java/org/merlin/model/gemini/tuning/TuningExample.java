package org.merlin.model.gemini.tuning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/tuning#tuningexample">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuningExample {
    private String output;
    private String textInput;
}
