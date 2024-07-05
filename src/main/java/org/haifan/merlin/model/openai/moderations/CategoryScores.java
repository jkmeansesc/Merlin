package org.haifan.merlin.model.openai.moderations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
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
