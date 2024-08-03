package org.merlin.model.gemini.generatingcontent;

import lombok.*;
import org.merlin.model.gemini.caching.Content;
import org.merlin.model.gemini.caching.Tool;
import org.merlin.model.gemini.caching.ToolConfig;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/tokens#generatecontentrequest">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateContentRequest {
    private String model;
    @NonNull
    private List<Content> contents;
    private List<Tool> tools;
    private ToolConfig toolConfig;
    private List<SafetySetting> safetySettings;
    private Content systemInstruction;
    private GenerationConfig generationConfig;
    private String cachedContent;
}
