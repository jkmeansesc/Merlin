package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.caching.Content;
import org.haifan.merlin.model.gemini.caching.Tool;
import org.haifan.merlin.model.gemini.caching.ToolConfig;

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
    private List<Content> contents;
    private List<Tool> tools;
    private ToolConfig toolConfig;
    private List<SafetySetting> safetySettings;
    private Content systemInstruction;
    private GenerationConfig generationConfig;
    private String cachedContent;
}
