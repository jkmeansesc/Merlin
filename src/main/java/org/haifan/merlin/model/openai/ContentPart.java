package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageUrlContentPart.class, name = "image_url"),
        @JsonSubTypes.Type(value = TextContentPart.class, name = "text"),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ContentPart {
    @NonNull
    private String type;
}