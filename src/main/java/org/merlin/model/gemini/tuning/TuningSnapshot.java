package org.merlin.model.gemini.tuning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/tuning#tuningsnapshot">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuningSnapshot {
    private String step;
    private Integer epoch;
    private Double meanLoss;
    private String computeTime;
}
