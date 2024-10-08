package org.merlin.model.openai.endpoints.moderations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/moderations/object#moderations/object-results">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moderation {

    private boolean flagged;

    private Categories categories;

    @JsonProperty("category_scores")
    private CategoryScores categoryScores;
}
