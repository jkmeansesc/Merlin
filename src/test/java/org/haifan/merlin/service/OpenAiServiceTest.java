package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.endpoints.Function;
import org.haifan.merlin.model.openai.endpoints.audio.SpeechRequest;
import org.haifan.merlin.model.openai.endpoints.chat.*;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageRequest;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationRequest;
import org.haifan.merlin.utils.JsonPrinter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class OpenAiServiceTest {

    @Test
    void testGetConfig() {
    }

    @Nested
    class TestChat {
        @Test
        void testChatCompletionRequest() {

            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage("system message"));
            messages.add(new UserMessage("user message"));
            messages.add(new ToolMessage("tool message", "toolCallId"));
            messages.add(new AssistantMessage("assistant message", "assistant name"));

            ChatCompletionRequest request_1 = ChatCompletionRequest.builder()
                    .model("model")
                    .messages(messages)
                    .toolChoice(new ToolChoice(new Tool("auto", new Function("function name"))))
                    .build();

            System.out.println(JsonPrinter.print(request_1));

            ChatCompletionRequest request_2 = ChatCompletionRequest.builder()
                    .model("model")
                    .messages(messages)
                    .toolChoice(new ToolChoice("123"))
                    .build();

            System.out.println(JsonPrinter.print(request_2));
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

            @Test
            void testUserMessage() {
                List<Message> messages = new ArrayList<>();

                UserMessage userMessageWithDefaultName = new UserMessage("test simple string content");
                messages.add(userMessageWithDefaultName);
                assertNotNull(messages.get(0).getRole());
                assertEquals(Fields.USER, messages.get(0).getRole());

                UserMessage userMessageWithName = new UserMessage("test simple string content with name", "simple name");
                messages.add(userMessageWithName);
                assertNotNull(messages.get(1).getRole());
                assertEquals(Fields.USER, messages.get(1).getRole());
                assertEquals("simple name", messages.get(1).getName());

                String imgUrl = "https://example.imgurl.com";
                String imgUrl_1 = "https://different.imgurl.com";
                String imgUrl_2 = "https://another.different.imgurl.com";
                String detail = "low";
                List<ContentPart> contentParts = new ArrayList<>();

                UserMessage userMessageWithContentPart = new UserMessage(contentParts)
                        .addTextContent("text content in TextContentPart")
                        .addImageContent(imgUrl)
                        .addImageContent(imgUrl_1, detail)
                        .addImageContent(imgUrl_2);

                assertEquals(Fields.TEXT, contentParts.get(0).getType());
                assertEquals(Fields.IMAGE_URL, contentParts.get(1).getType());

                messages.add(userMessageWithContentPart.setContentParts(contentParts));
                assertNotNull(messages.get(2).getRole());
                assertEquals(Fields.USER, messages.get(2).getRole());

                UserMessage userMessageWithDefault = new UserMessage()
                        .addTextContent("text content")
                        .addImageContent(imgUrl, "high")
                        .addImageContent(imgUrl_1, "whatever");
                messages.add(userMessageWithDefault);
                assertNotNull(messages.get(3).getRole());
                assertEquals(Fields.USER, messages.get(3).getRole());

                UserMessage userMessageWithContentPartAndName = new UserMessage(new ArrayList<>(), "name");
                messages.add(userMessageWithContentPartAndName);

                System.out.println(JsonPrinter.print(messages));
            }

            @Test
            void testAssistantMessage() {

            }
        }
    }

    @Nested
    class TestEmbedding {
        @Test
        void createEmbeddings() {

            // Single string
            EmbeddingRequest request1 = EmbeddingRequest.builder()
                    .input("This is a test string")
                    .dimensions(1)
                    .encodingFormat("base64")
                    .model("text-embedding-3")
                    .build();
            System.out.println(JsonPrinter.print(request1));

            // Array of strings
            List<String> stringList = Arrays.asList("String 1", "String 2", "String 3");
            EmbeddingRequest request2 = EmbeddingRequest.builder()
                    .input(stringList)
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(JsonPrinter.print(request2));

            // Array of integers
            List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
            EmbeddingRequest request3 = EmbeddingRequest.builder()
                    .input(intList)
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(JsonPrinter.print(request3));

            // Array of arrays of integers
            List<List<Integer>> nestedList = Arrays.asList(
                    Arrays.asList(1, 2, 3),
                    Arrays.asList(4, 5, 6),
                    Arrays.asList(7, 8, 9)
            );

            EmbeddingRequest request4 = EmbeddingRequest.builder()
                    .input(nestedList)
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(JsonPrinter.print(request4));

            EmbeddingRequest request5 = EmbeddingRequest.builder()
                    .input(new Integer[]{1, 2, 3})
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(JsonPrinter.print(request5));

            EmbeddingRequest request6 = EmbeddingRequest.builder()
                    .input(new String[]{"string1", "string2", "string3"})
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(JsonPrinter.print(request6));

            Merlin.<OpenAiService>builder()
                    .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                    .build()
                    .getService()
                    .createEmbeddings(request1)
                    .join();
        }
    }

    @Nested
    class TestFineTuning {
        @Test
        void createFineTuning() {

        }

        @Test
        void listFineTuningJobs() {

        }

        @Test
        void listFineTuningEvents() {

        }

        @Test
        void listFineTuningCheckpoints() {

        }

        @Test
        void retrieveFineTuningJob() {

        }

        @Test
        void cancelFineTuningJob() {

        }
    }

    @Nested
    class TestBatch {

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

    @Nested
    class AssistantTest {

    }
}