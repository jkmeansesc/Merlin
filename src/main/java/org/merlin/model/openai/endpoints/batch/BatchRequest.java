package org.merlin.model.openai.endpoints.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * <a href="https://platform.openai.com/docs/api-reference/batch/create">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchRequest {
    @NonNull
    @JsonProperty("input_file_id")
    private String inputFileId;

    @NonNull
    private String endpoint;

    @NonNull
    @JsonProperty("completion_window")
    private String completionWindow;

    private Map<String, Object> metadata;
}
