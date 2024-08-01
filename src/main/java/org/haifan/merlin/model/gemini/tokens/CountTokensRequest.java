package org.haifan.merlin.model.gemini.tokens;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.caching.Content;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentRequest;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/tokens#request-body">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountTokensRequest {
    private List<Content> contents;
    private GenerateContentRequest generateContentRequest;
}
