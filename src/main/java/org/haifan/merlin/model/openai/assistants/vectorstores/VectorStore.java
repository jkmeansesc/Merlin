package org.haifan.merlin.model.openai.assistants.vectorstores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.model.openai.assistants.ExpiresAfter;
import org.haifan.merlin.model.openai.assistants.FileCounts;

import java.util.Map;

@Data
public class VectorStore {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Long createdAt;
    private String name;
    @JsonProperty("usage_bytes")
    private Long usageBytes;
    @JsonProperty("file_counts")
    private FileCounts fileCounts;
    private String status;
    @JsonProperty("expires_after")
    private ExpiresAfter expiresAfter;
    @JsonProperty("expires_at")
    private Long expiresAt;
    @JsonProperty("last_active_at")
    private Long lastActiveAt;
    private Map<String, Object> metadata;
}