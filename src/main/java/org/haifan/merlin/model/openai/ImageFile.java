package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageFile {
    @NonNull
    @JsonProperty("file_id")
    private String fileId;
    private String detail;
}
