package org.merlin.model.openai.endpoints.moderations;

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
public class CategoryScores {

    private double hate;

    @JsonProperty("hate/threatening")
    private double hateThreatening;

    private double harassment;

    @JsonProperty("harassment/threatening")
    private double harassmentThreatening;

    @JsonProperty("self-harm")
    private double selfHarm;

    @JsonProperty("self-harm/intent")
    private double selfHarmIntent;

    @JsonProperty("self-harm/instructions")
    private double selfHarmInstructions;

    private double sexual;

    @JsonProperty("sexual/minors")
    private double sexualMinors;

    private double violence;

    @JsonProperty("violence/graphic")
    private double violenceGraphic;
}
