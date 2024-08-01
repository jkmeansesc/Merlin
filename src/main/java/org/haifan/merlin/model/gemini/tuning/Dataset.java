package org.haifan.merlin.model.gemini.tuning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/tuning#dataset">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {
    private TuningExamples examples;
}
