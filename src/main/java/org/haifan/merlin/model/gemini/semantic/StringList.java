package org.haifan.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/documents#stringlist">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringList {
    List<String> values;
}
