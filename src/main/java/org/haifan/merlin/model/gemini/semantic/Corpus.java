package org.haifan.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/corpora#resource:-corpus">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Corpus {
    private String name;
    private String displayName;
    private String createTime;
    private String updateTime;
}
