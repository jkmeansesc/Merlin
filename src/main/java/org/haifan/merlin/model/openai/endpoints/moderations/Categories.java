package org.haifan.merlin.model.openai.endpoints.moderations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/moderations/object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    private boolean hate;

    @JsonProperty("hate/threatening")
    private boolean hateThreatening;

    private boolean harassment;

    @JsonProperty("harassment/threatening")
    private boolean harassmentThreatening;

    @JsonProperty("self-harm")
    private boolean selfHarm;

    @JsonProperty("self-harm/intent")
    private boolean selfHarmIntent;

    @JsonProperty("self-harm/instructions")
    private boolean selfHarmInstructions;

    private boolean sexual;

    @JsonProperty("sexual/minors")
    private boolean sexualMinors;

    private boolean violence;

    @JsonProperty("violence/graphic")
    private boolean violenceGraphic;

}
