package org.merlin.model.openai;

import lombok.*;
import org.merlin.internal.constants.Fields;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class AutoChunkingStrategy extends ChunkingStrategy {

    public AutoChunkingStrategy() {
        super(Fields.AUTO);
    }
}
