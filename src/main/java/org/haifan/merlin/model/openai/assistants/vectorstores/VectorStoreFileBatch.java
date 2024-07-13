package org.haifan.merlin.model.openai.assistants.vectorstores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.model.openai.assistants.FileCounts;

@Data
public class VectorStoreFileBatch {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("vector_store_id")
    private String vectorStoreId;
    private String status;
    @JsonProperty("file_counts")
    private FileCounts fileCounts;
}
