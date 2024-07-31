package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.*;

@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
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

    @JsonCreator
    protected ContentPart(@NonNull @JsonProperty("type") String type) {
        this.type = type;
    }
}