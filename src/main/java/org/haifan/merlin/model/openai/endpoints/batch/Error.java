package org.haifan.merlin.model.openai.endpoints.batch;

import lombok.Data;

@Data
public class Error {
    private String code;
    private String message;
    private String param;
    private Integer line;
}
