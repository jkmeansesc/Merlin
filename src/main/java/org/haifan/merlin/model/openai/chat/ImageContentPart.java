package org.haifan.merlin.model.openai.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageContentPart extends ContentPart {

    @NonNull
    @JsonProperty("image_url")
    private ImageUrl imageUrl;

    public ImageContentPart(String url) {
        super(Fields.IMAGE_URL);
        this.imageUrl = new ImageUrl(url);
    }

    public ImageContentPart(String url, String detail) {
        super(Fields.IMAGE_URL);
        this.imageUrl = new ImageUrl(url, detail);
    }

    @Data
    public static class ImageUrl {
        @NonNull
        private String url;
        private String detail;

        public ImageUrl(@NotNull String url) {
            this.url = url;
        }

        public ImageUrl(@NotNull String url, String detail) {
            this.url = url;
            this.detail = detail;
        }
    }
}
