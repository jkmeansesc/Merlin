package org.haifan.merlin.model.openai.images;

import lombok.Data;

import java.util.List;

/**
 * The image object that contains a list of {@link Image} result.
 * <a href="https://platform.openai.com/docs/api-reference/images/create">...</a>
 */
@Data
public class ImageList {
    private String created;
    private List<Image> data;
}
