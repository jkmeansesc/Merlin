package org.haifan.merlin.model.openai.endpoints.moderations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents if a given text input is potentially harmful.
 * <a href="https://platform.openai.com/docs/api-reference/moderations/object#moderations/object-results">...</a>
 */
@Data
public class Moderation {

    private boolean flagged;

    private Categories categories;

    @JsonProperty("category_scores")
    private CategoryScores categoryScores;
}
