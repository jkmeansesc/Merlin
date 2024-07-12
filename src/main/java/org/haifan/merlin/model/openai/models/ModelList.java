package org.haifan.merlin.model.openai.models;

import lombok.Data;

import java.util.List;

/**
 * The model response object that contains a list of {@link Model} results.
 * <a href="https://platform.openai.com/docs/api-reference/models/list">...</a>
 */
@Data
public class ModelList {
    private String object;
    private List<Model> data;
}
