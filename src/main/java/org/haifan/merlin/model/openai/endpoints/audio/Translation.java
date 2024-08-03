package org.haifan.merlin.model.openai.endpoints.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/createTranslation">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Translation {
    private String text;
}
