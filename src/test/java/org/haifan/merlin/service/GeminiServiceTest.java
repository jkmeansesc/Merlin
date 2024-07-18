package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.model.gemini.ModelList;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GeminiServiceTest {

    @Nested
    class V1Test {
        @Test
        void listModels() {
            ModelList list = Merlin.<GeminiService>builder()
                    .service(new GeminiService("AIzaSyBK8wku8kgvri-Q-LAE9uPFU28gtxdwWG4"))
                    .build()
                    .getService()
                    .listModels()
                    .join();
            System.out.println(list);
        }
    }
}