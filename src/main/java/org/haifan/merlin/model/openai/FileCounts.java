package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileCounts {
    @JsonProperty("in_progress")
    private Integer inProgress;
    private Integer completed;
    private Integer failed;
    private Integer cancelled;
    private Integer total;
}
