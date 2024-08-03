package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Annotation {
    private String type;
    private String text;
    @JsonProperty("file_citation")
    private FileCitation fileCitation;
    @JsonProperty("file_path")
    private FilePath filePath;
    @JsonProperty("start_index")
    private Integer startIndex;
    @JsonProperty("end_index")
    private Integer endIndex;
}
