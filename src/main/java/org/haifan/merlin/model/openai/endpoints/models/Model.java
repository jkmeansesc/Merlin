package org.haifan.merlin.model.openai.endpoints.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/models/object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Model {
    private String id;

    private String object;

    private Long created;

    @JsonProperty("owned_by")
    private String ownedBy;
}
