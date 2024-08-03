package org.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.gemini.BlockReason;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/generate-content#promptfeedback">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptFeedback {
    private BlockReason blockReason;
    private List<SafetyRating> safetyRatings;
}
