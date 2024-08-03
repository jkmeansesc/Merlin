package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TextContentPart extends ContentPart {
    @NonNull
    private Text text;
    private Integer index;

    @JsonCreator
    public TextContentPart(
            @NonNull @JsonProperty("text") Text text,
            @JsonProperty("index") Integer index) {
        super("text");
        this.text = text;
        this.index = index;
    }
}
