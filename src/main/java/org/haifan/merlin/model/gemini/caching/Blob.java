package org.haifan.merlin.model.gemini.caching;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blob {
    private String mimeType;
    private String data;
}
