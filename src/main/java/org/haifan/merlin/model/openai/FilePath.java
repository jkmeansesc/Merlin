package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FilePath {
    @JsonProperty("file_id")
    private String fileId;
}
