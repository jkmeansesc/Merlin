package org.haifan.merlin.model.openai.endpoints.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/files/object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiFile {

    private String id;

    private Long bytes;

    @JsonProperty("created_at")
    private Long createdAt;

    private String filename;

    private String object;

    private String purpose;

    /**
     * @deprecated deprecated from openai documentation, will be removed if openai stops returning this field.
     */
    @Deprecated(since = "1.0", forRemoval = true)
    private String status;

    /**
     * @deprecated deprecated from openai documentation, will be removed if openai stops returning this field.
     */
    @Deprecated(since = "1.0", forRemoval = true)
    @JsonProperty("status_details")
    private String statusDetails;

}
