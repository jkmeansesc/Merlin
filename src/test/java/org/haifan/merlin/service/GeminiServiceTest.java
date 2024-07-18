package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GeminiServiceTest {

    @Nested
    class V1Test {
        @Test
        void listModels() {
            Merlin.builder()
                    .addService(new GeminiService("AIzaSyBK8wku8kgvri-Q-LAE9uPFU28gtxdwWG4"))
                    .build()
                    .getGeminiService()
                    .listModels()
                    .join();
        }
    }
}