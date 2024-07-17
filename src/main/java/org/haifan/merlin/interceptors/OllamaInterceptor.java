package org.haifan.merlin.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.haifan.merlin.constants.Provider;

@Slf4j
public class OllamaInterceptor extends LlmInterceptor {

    public OllamaInterceptor() {
        super();
    }

    @Override
    protected Request addAuthHeader(Request originalRequest) {
        log.info("No auth header needed for {}", Provider.OLLAMA);
        return originalRequest;
    }
}
