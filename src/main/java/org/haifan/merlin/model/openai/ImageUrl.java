package org.haifan.merlin.model.openai;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUrl {
    @NonNull
    private String url;
    private String detail;
}
