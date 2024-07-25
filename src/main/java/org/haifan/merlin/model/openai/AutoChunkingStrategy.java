package org.haifan.merlin.model.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.internal.constants.Fields;

@EqualsAndHashCode(callSuper = true)
@Data
public class AutoChunkingStrategy extends ChunkingStrategy {

    public AutoChunkingStrategy() {
        super(Fields.AUTO);
    }
}
