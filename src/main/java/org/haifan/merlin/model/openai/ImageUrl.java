package org.haifan.merlin.model.openai;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUrl {
    @NonNull
    private String url;
    private String detail;
}
