package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageFileContentPart extends ContentPart {

    @NonNull
    @JsonProperty("image_file")
    private ImageFile imageFile;

    public ImageFileContentPart(String fileId) {
        super(Fields.IMAGE_FILE);
        this.imageFile = new ImageFile(fileId);
    }

    public ImageFileContentPart(String fileId, String detail) {
        super(Fields.IMAGE_FILE);
        this.imageFile = new ImageFile(fileId, detail);
    }

    @Data
    public static class ImageFile {
        @NonNull
        @JsonProperty("file_id")
        private String fileId;
        private String detail;

        public ImageFile(@NotNull String fileId) {
            this.fileId = fileId;
        }

        public ImageFile(@NotNull String fileId, String detail) {
            this.fileId = fileId;
            this.detail = detail;
        }
    }
}
