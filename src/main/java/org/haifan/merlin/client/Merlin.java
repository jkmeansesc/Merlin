package org.haifan.merlin.client;

import lombok.Builder;
import lombok.Getter;
import org.haifan.merlin.service.LlmService;

/**
 * TODO: add javadoc
 *
 */
@Getter
@Builder
public class Merlin<T extends LlmService> {

    private final T service;

    @SuppressWarnings("unused")
    public static class MerlinBuilder<T extends LlmService> {
        // Lombok will generate the builder methods
    }
}
