package org.haifan.merlin.model.openai.endpoints.audio;

import lombok.Data;

import java.util.List;

/**
 * Represents a (verbose) transcription response returned by model, based on the provided input.
 * <a href="https://platform.openai.com/docs/api-reference/audio/verbose-json-object">...</a>
 */
@Data
public class Transcription {

    private String task;

    private String language;

    private Double duration;

    private String text;

    private List<Words> words;

    private List<Segment> segments;
}
