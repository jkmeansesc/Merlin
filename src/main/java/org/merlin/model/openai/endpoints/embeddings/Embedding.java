package org.merlin.model.openai.endpoints.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/embeddings/object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Embedding {
    private Integer index;
    @JsonProperty("embedding")
    private List<Float> embeddingVector;
    private String object;
}
