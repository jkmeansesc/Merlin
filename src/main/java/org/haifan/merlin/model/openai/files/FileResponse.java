package org.haifan.merlin.model.openai.files;

import lombok.Data;

import java.util.List;

/**
 * The file response object that contains a list of {@link File} results.
 * <a href="https://platform.openai.com/docs/api-reference/files/list">...</a>
 */
@Data
public class FileResponse {
    private String object;
    private List<File> data;
}
