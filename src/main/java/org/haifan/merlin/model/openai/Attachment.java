package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Attachment {
    @JsonProperty("file_id")
    private String fileId;
    private List<Tool> tools;
}
