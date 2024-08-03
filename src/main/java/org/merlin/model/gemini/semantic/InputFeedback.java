package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.gemini.BlockReason;
import org.merlin.model.gemini.generatingcontent.SafetyRating;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/question-answering#inputfeedback">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputFeedback {
    List<SafetyRating> safetyRatings;
    private BlockReason blockReason;
}
