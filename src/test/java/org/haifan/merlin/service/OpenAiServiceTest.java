package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.model.openai.images.CreateImageRequest;
import org.haifan.merlin.model.openai.moderations.ModerationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OpenAiServiceTest {

    @Test
    void testGetConfig() {
    }

    @Nested
    class TestImages {

        @Test
        void createImage() {
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .createImage(CreateImageRequest.builder()
                            .prompt("A cute baby sea otter")
                            .n(1)
                            .build())
                    .join();
        }
    }

    @Nested
    class TestModels {

        @Test
        void testListModels() {
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .listModels()
                    .join();
        }

        @Test
        void testRetrieveModel() {
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .retrieveModel("gpt-3.5-turbo-instruct")
                    .join();
        }

        @Test
        void testDeleteAFineTunedModel() {
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .deleteAFineTunedModel("ft:gpt-3.5-turbo:acemeco:suffix:abc123")
                    .join();
        }
    }

    @Nested
    class ModerationTest {
        @Test
        void testCreateModeration() {
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .createModeration(ModerationRequest
                            .builder()
                            .input("I want to kill them.")
                            .model("text-moderation-stable")
                            .build())
                    .join();
        }
    }
}