package org.merlin.model.gemini.generatingcontent;

import lombok.*;
import org.merlin.model.gemini.UsageMetadata;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/generate-content#generatecontentresponse">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateContentResponse {
    private List<Candidate> candidates;
    private PromptFeedback promptFeedback;
    private UsageMetadata usageMetadata;
}
