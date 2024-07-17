package org.haifan.merlin.model.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenerateContentRequest {
    private List<Content> contents;
    private List<SafetySetting> safetySettings;
    private GenerationConfig generationConfig;
}
