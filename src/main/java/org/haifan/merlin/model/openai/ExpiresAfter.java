package org.haifan.merlin.model.openai;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiresAfter {
    @NonNull
    private String anchor;
    @NonNull
    private Integer days;
}
