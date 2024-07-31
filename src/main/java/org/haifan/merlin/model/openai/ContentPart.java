package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageUrlContentPart.class, name = "image_url"),
        @JsonSubTypes.Type(value = ImageFileContentPart.class, name = "image_file"),
        @JsonSubTypes.Type(value = TextContentPart.class, name = "text"),
})
public abstract class ContentPart {
    @NonNull
    private String type;
}