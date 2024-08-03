package org.merlin.model.openai.endpoints.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/verbose-json-object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transcription {
    private String task;
    private String language;
    private Double duration;
    private String text;
    private List<Words> words;
    private List<Segment> segments;
}
