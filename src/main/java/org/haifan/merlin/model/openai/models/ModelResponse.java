package org.haifan.merlin.model.openai.models;

import lombok.Data;

import java.util.List;

/**
 * Lists the currently available models, and provides basic information about each one such as the owner and availability.
 * <a href="https://platform.openai.com/docs/api-reference/models/list">...</a>
 */
@Data
public class ModelResponse {
    private String object;
    private List<Model> data;
}
