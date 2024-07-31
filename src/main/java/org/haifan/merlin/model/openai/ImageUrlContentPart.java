package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ImageUrlContentPart extends ContentPart {
    @NonNull
    @JsonProperty("image_url")
    private ImageUrl imageUrl;
    private Integer index;

    @JsonCreator
    public ImageUrlContentPart(
            @NonNull @JsonProperty("image_url") ImageUrl imageUrl,
            @JsonProperty("index") Integer index) {
        super("image_url");
        this.imageUrl = imageUrl;
        this.index = index;
    }
}
