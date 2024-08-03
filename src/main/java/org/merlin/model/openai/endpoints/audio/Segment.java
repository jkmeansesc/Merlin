package org.merlin.model.openai.endpoints.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/verbose-json-object#audio/verbose-json-object-segments">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Segment {
    private Integer id;
    private Integer seek;
    private Double start;
    private Double end;
    private String text;
    private List<Integer> tokens;
    private Double temperature;
    @JsonProperty("avg_logprob")
    private Double averageLogProb;
    @JsonProperty("compression_ratio")
    private Double compressionRatio;
    @JsonProperty("no_speech_prob")
    private Double noSpeechProb;
}
