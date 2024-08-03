package org.merlin.model.gemini.tuning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/tuning#tuningtask">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuningTask {
    private String startTime;
    private String completeTime;
    private List<TuningSnapshot> snapshots;
    private Dataset trainingData;
    private Hyperparameters hyperparameters;
}
