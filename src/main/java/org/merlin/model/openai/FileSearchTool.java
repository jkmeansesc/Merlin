package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.merlin.internal.constants.Fields;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class FileSearchTool extends AssistantTool {

    @JsonProperty("file_search")
    private FileSearch fileSearch;

    public FileSearchTool() {
        super(Fields.FILE_SEARCH);
    }

    public FileSearchTool(FileSearch fileSearch) {
        super(Fields.FILE_SEARCH);
        this.fileSearch = fileSearch;
    }


    @Data
    public static class FileSearch {
        @JsonProperty("max_num_results")
        private Integer maxNumResults;
    }
}
