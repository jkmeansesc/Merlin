package org.haifan.merlin.model.openai.assistants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.constants.Fields;

@EqualsAndHashCode(callSuper = true)
@Data
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
