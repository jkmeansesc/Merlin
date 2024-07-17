package org.haifan.merlin.model.gemini.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.Content;
import org.haifan.merlin.model.gemini.GenerationConfig;
import org.haifan.merlin.model.gemini.SafetySetting;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CountTokensRequest {
    private List<Content> contents;
    private GenerateContentRequest generateContentRequest;

    @Data
    public static class GenerateContentRequest {
        private String model;
        private List<Content> contents;
        private List<SafetySetting> safetySettings;
        private GenerationConfig generationConfig;
    }
}
