package org.haifan.merlin.model.openai.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The File object represents a document that has been uploaded to OpenAI.
 * <a href="https://platform.openai.com/docs/api-reference/files/object">...</a>
 */
@Data
public class File {

    private String id;

    private Long bytes;

    @JsonProperty("created_at")
    private Long createdAt;

    private String filename;

    private String object;

    private String purpose;

}
