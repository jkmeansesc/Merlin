package org.haifan.merlin.model.openai;

import lombok.Data;

@Data
public class LastError {
    private String code;
    private String message;
}
