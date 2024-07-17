package org.haifan.merlin.model.gemini;

import lombok.Data;

import java.util.List;

@Data
public class Status {
    private Integer code;
    private String message;
    private List<ErrorDetail> details;
}
