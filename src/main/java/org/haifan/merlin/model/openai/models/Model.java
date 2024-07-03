package org.haifan.merlin.model.openai.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * GPT model details
 * <a href="https://platform.openai.com/docs/api-reference/models/object">...</a>
 */
@Data
public class Model {
    /**
     * The model identifier, which can be referenced in the API endpoints.
     */
    private String id;

    /**
     * The type of object returned, should be "model"
     */
    private String object;

    /**
     * The Unix timestamp (in seconds) when the model was created.
     */
    private Integer created;

    /**
     * The organization that owns the model.
     */
    @JsonProperty("owned_by")
    private String ownedBy;
}
