package org.haifan.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.UsageMetadata;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/caching#resource:-cachedcontent">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedContent {
    private List<Content> contents;
    private List<Tool> tools;
    private String createTime;
    private String updateTime;
    private UsageMetadata usageMetadata;
    private String expireTime;
    private String ttl;
    private String name;
    private String displayName;
    private String model;
    private Content systemInstruction;
    private ToolConfig toolConfig;
}
