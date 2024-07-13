package org.haifan.merlin.model.openai.endpoints.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
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
