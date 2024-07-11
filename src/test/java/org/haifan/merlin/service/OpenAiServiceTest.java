package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.audio.SpeechRequest;
import org.haifan.merlin.model.openai.chat.*;
import org.haifan.merlin.model.openai.images.ImageRequest;
import org.haifan.merlin.model.openai.moderations.ModerationRequest;
import org.haifan.merlin.utils.JsonPrinter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class OpenAiServiceTest {

    @Test
    void testGetConfig() {
    }

    @Nested
    class TestChat {
        @Test
        void testChatCompletionRequest() {
//            ToolChoiceObject toolChoiceObject = new ToolChoiceObject();
//            ChatCompletionRequest request = ChatCompletionRequest.builder()
//                    .toolChoiceString("123")
//                    .toolChoiceObject(toolChoiceObject)
//                    .build();
//            System.out.println(request);

        }

        @Nested
        class TestMessage {
            @Test
            void testSystemMessage() {

                String content = "System content";
                String name = "System name";

                List<Message> messages = new ArrayList<>();
                messages.add(new SystemMessage(content));
                messages.add(new SystemMessage(content, name));

                assertNotNull(messages.get(0).getRole());
                assertEquals(Fields.SYSTEM, messages.get(0).getRole());
                assertNotNull(messages.get(1).getRole());
                assertEquals(Fields.SYSTEM, messages.get(1).getRole());
                assertNull(messages.get(0).getName());
                assertEquals(name, messages.get(1).getName());

                System.out.println(JsonPrinter.print(messages));
            }
        }
    }

    @Nested
    class TestAudio {
        @Test
        void createSpeech() {
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .createSpeech(SpeechRequest
                            .builder()
                            .model("tts-1")
                            .input("The quick brown fox jumped over the lazy dog.")
                            .voice("alloy")
                            .build())
                    .join();
        }
    }

    @Nested
    class TestFiles {
        @Test
        void uploadFile() {
            URL resourceUrl = OpenAiServiceTest.class.getClassLoader().getResource("log4j2.xml");
            assert resourceUrl != null;
            String filePath = Paths.get(resourceUrl.getPath()).toFile().getAbsolutePath();
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .uploadFile("fine-tune", filePath)
                    .join();
        }
    }


    @Nested
    class TestImages {

        @Test
        void createImage() {
            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .createImage(ImageRequest.builder()
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
                    .createModeration(ModerationRequest.builder()
                            .input("I want to kill them.")
                            .model("text-moderation-stable")
                            .build())
                    .join();
        }
    }
}