package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/question-answering#groundingpassages">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroundingPassages {
    List<GroundingPassage> passages;
}
