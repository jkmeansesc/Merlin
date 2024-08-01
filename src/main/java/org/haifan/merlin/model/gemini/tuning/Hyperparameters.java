package org.haifan.merlin.model.gemini.tuning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/tuning#hyperparameters">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hyperparameters {
    private Double learningRate;
    private Double learningRateMultiplier;
    private Integer epochCount;
    private Integer batchSize;
}
