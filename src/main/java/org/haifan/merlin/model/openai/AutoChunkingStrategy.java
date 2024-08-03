package org.haifan.merlin.model.openai;

import lombok.*;
import org.haifan.merlin.internal.constants.Fields;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class AutoChunkingStrategy extends ChunkingStrategy {

    public AutoChunkingStrategy() {
        super(Fields.AUTO);
    }
}
