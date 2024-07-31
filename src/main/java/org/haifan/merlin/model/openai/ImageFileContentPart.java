package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ImageFileContentPart extends ContentPart {
    @NonNull
    @JsonProperty("image_file")
    private ImageFile imageFile;
    private Integer index;

    @JsonCreator
    public ImageFileContentPart(
            @NonNull @JsonProperty("image_file") ImageFile imageFile,
            @JsonProperty("index") Integer index) {
        super("image_file");
        this.imageFile = imageFile;
        this.index = index;
    }
}
