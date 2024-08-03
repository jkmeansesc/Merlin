package org.merlin.internal.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.merlin.internal.constants.Provider;

/**
 * A just-in-case implementation against future changes from Ollama, also to fit the over-all design.
 */
@Slf4j
public class OllamaInterceptor extends AuthenticationInterceptor {

    public OllamaInterceptor() {
        super();
    }

    @Override
    protected Request addAuthHeader(Request originalRequest) {
        log.info("No auth header needed for {}", Provider.OLLAMA);
        return originalRequest;
    }
}
