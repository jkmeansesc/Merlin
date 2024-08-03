package org.merlin.model.openai.assistants.vectorstores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.openai.FileCounts;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
