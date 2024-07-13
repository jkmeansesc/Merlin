package org.haifan.merlin.model.openai.endpoints.batch;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * The request model object to create batch for OpenAI.
 * <a href="https://platform.openai.com/docs/api-reference/batch/create">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchRequest {
    @NonNull
    @JsonProperty("input_file_id")
    private String inputFileId;

    @NonNull
    private String endpoint;

    @NonNull
    @JsonProperty("completion_window")
    private Integer completionWindow;

    private Map<String, Object> metadata;
}
