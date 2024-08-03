package org.merlin.model.gemini.tuning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/tuning#tuningexamples">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuningExamples {
    List<TuningExample> examples;
}
