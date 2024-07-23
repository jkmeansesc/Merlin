# Merlin

Merlin is a Java util library that provides an unified interface to interact with Large Language Models (LLMs).

| Libraries used                                           |
| -------------------------------------------------------- |
| [okhttp](https://github.com/square/okhttp)               |
| [log4j](https://logging.apache.org/log4j/2.x/index.html) |
| [jackson](https://github.com/FasterXML/jackson)          |
| [lombok](https://projectlombok.org/)                     |
| [JUnit5](https://junit.org/junit5/)                      |
| [Mockito](https://site.mockito.org/)                     |
| [SLF4J](https://www.slf4j.org/index.html)                |

> OpenAI: deprecated and legacy features are not supported

> Google Gemini: currently supports v1beta.

## Getting Started

> This section will be updated as the project progresses and in sequential order.

### Add Merlin to your project

#### Maven

```xml
TBA
```

#### Gradle

```gradle
TBA
```

### API Key Management

Merlin supports API key management through environment variables.

| Provider       | Environment Variable |
| -------------- | -------------------- |
| OpenAI ChatGPT | `OPENAI_KEY`         |
| Goole Gemini   | `GOOGLE_GEMINI_KEY`  |

Or you can set the API key directly through initialization.

```java
OpenAiService service = new OpenAiService("<your-api-key>");
GenminiService service = new GeminiService("<your-api-key>");
```

By default, Merlin will look for API keys through environment variables and fall back to direct initialization. If neither is found, an Exception will be thrown.

## User stories

> Expected to change during development. Some user stories might be broken down into smaller user stories.
> I will update the user stories when I finished unit testing.

| 1. Basic Integration                                                                                                              | Must have                                                                                                                                                                                                            |
| --------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| "As a developer, I want to integrate various LLMs into my Java application using Merlin, so I can use multiple AI models easily." | **Acceptance Criteria:** The library should provide a simple API to connect to at least three different LLM providers (e.g., OpenAI, Google Gemini, Ollama). The API should be well-documented with usage examples." |

| 2. Sending Requests                                                                                                                        | Must have                                                                                                                                                                                                               |
| ------------------------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| "As a developer, I want to send text queries to an LLM and receive responses, so I can utilize the AI capabilities within my application." | **Acceptance Criteria:** The library should support sending a text query to a connected LLM. The library should return the LLM’s response in a standard format. The response time should be optimized for performance." |

| 3. Handling Multiple Models                                                                                        | Must have                                                                                                                  |
| ------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------- |
| "As a developer, I want to switch between different LLMs seamlessly, so I can choose the best model for my needs." | **Acceptance Criteria:** The library should support switching between different LLM providers without major code changes." |

| 4. Error Handling                                                                                                                 | Must have                                                                                                                                                                                                          |
| --------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| "As a developer, I want robust error handling when making API calls, so I can ensure my application handles failures gracefully." | **Acceptance Criteria:** The library should provide clear error messages for different failure scenarios (e.g., network issues, API rate limits). The library should offer retry mechanisms and fallback options." |

| 5. Logging and Monitoring                                                                                                       | Must have                                                                                                                                                                                                                            |
| ------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| "As a developer, I want to log API requests and responses, so I can monitor the usage and performance of the LLM integrations." | **Acceptance Criteria:** The library should integrate with a logging framework log4j to log all API interactions. The logs should include relevant information like request timestamps, response times, and any errors encountered." |

| 6. Security                                                                                                   | Must have                                                                                                                                                                                       |
| ------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| "As a developer, I want to securely handle API keys and credentials, so I can protect sensitive information." | **Acceptance Criteria:** The library should provide secure methods to store and use API keys. The library should ensure that sensitive information is not logged or exposed in error messages." |

| 7. Configuration Management                                                                                | Must have                                                                                                                                                                  |
| ---------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| "As a developer, I want to configure the library easily, so I can adjust settings without modifying code." | **Acceptance Criteria:** The library should support configuration through properties files or environment variables. The configuration options should be well-documented." |

| 8. Documentation                                                                                                                   | Must have                                                                                                                                                                                                                                           |
| ---------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| "As a developer, I want comprehensive documentation and example receipes, so I can understand how to use the library effectively." | **Acceptance Criteria:** The library should include a detailed README file with installation instructions. There should be example projects demonstrating common use cases. The API should be well-documented with usage examples for each method." |

| 9. Asynchronous Operations                                                                                         | Must have                                                                                                                                                                                                                 |
| ------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| "As a developer, I want to perform asynchronous API calls, so I can improve the responsiveness of my application." | **Acceptance Criteria:** The library should support asynchronous requests and provide callback mechanisms. The library should allow for easy integration with Java’s CompletableFuture or similar concurrency utilities." |

## UML Diagram

```mermaid
classDiagram
    direction TD

    note for OpenAiApi "Only example listed"
    namespace api{
        class OpenAiApi {
            Call ~OpenAiResponse~Model~~ listModels();
        }
    }

    note for Merlin "TBD"
    namespace client{
        class Merlin {
            -Provider currentProvider
            +Merlin useProvider(Provider provider)
            +OpenAiService service
        }

        class Provider {
            <<enumeration>>
            OPENAI
            GOOGLE_GEMINI
        }
    }
    Merlin *-- Provider
    Merlin --> OpenAiResponse

    note for OpenAiService "Only example endpoint listed"
    namespace service{
        class LlmService {
            <<abstract>>
            - Retrofit retrofit
            + LlmService(String baseUrl, String apiKey)
            # ~T~ CompletableFuture~T~ executeCall(Call~T~ call)
        }
        class OpenAiService {
            <<service>>
            - OpenAiApi api
            + OpenAiService(String baseUrl, String apiKey)
            + CompletableFuture~OpenAiResponse~Model~~ listModels()
        }
    }
    OpenAiService --|> LlmService
    OpenAiApi --o OpenAiService


    note for OpenAiResponse "Only example listed"
    namespace model{
        class OpenAiResponse~T~ {
        +List~T~ data
        +String object
        +String firstId
        +String lastId
        +boolean hasMore
        }
    }

    note for Config "TBD"
    namespace config {
        class Config
    }

    namespace util {
        class JsonPrinter {
            + print(Object obj)
        }
    }
```

## Dev Logs

### 2024-06-14

Project needs more clarifications, needs to show more value.

- [x] write up a document explaining what the project is all about

### 2024-06-20

Project is approved. Do this for the next week:

- [x] naming the project
- [x] set up a Gitlab repository
- [x] start writing user stories
- [x] start designing
- [x] start building
- [x] send a weekly update next Wednesday
- [x] wait for an online meeting invite next Thursday, it's gonna be short since supervisor is on leave
- [x] fill out project tracker

Leave at least 3 weeks to start writing dissertation.

### 2024-07-06

- Added OpenAI endpoints for images.
- Added OpenAI endpoints for files.
- Added OpenAI endpoints for models.
- Added OpenAI endpoints for moderations.
- Added key management service: can read from environment variable or a given string directly.
- Created `SecureLoggingInterceptor` to mask sensitive headers.
- Set up `JUnit 5` for testing.
- Introduced logging framework.
- Created inner class to group test cases.
- Switched to SLF4J to provide more flexibility instead of Log4j 2.
- Solved console not logging "application/json" header defined in Retrofit interface.
- Solved Okhttp request serializing null fields into payload.

I don't need to handle `Content-Type` explicitly, `Okhttp` will check what I'm sending and update the headers accordingly.

- Added `FileParser` util to parse a `File` object into a `RequestBody`
- Dropped custom logging interceptor, use `HttpLoggingInterceptor` instead.

### 2024-07-07

- Added OpenAI endpoints for audios.

### 2024-07-11

Chat Completion Object is provided in a single, complete object, received after the model has finished generating the entire response. Chat Completion Chunk Object is split into multiple parts (chunks) and sent incrementally, received in real-time as the model generates the response, chunk by chunk, allowing for incremental processing.

Trying to find a way to stream and model the endpoints for chat.

Understanding what is backpressure, which is the remote producing response faster than the local can process.

Learn how to stream.

### 2024-07-12

- Added OpenAI endpoints and models for chat.
- Added OpenAI endpoints and models for embeddings.
- Added OpenAI endpoints and models for fine-tuning.
- Added OpenAI endpoints and models for batch.

### 2024-07-13

If a request has query parameters, provide a default method to call without them and one with all of them. When calling with query params, set null to the ones you don't need, Retrofit will ignore them in the call.

- Added OpenAI endpoints and models for Vector Store.
- Added OpenAI endpoints and models for Vector Store Files.
- Added OpenAI endpoints and models for Vector Store File Batches.

Many fields accept both String or a structured object, need to find a way to accommodate this.

Figured out how to use Jackson's polymorphic deserialization.

```java
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageContentPart.class, name = "image_url"),
        @JsonSubTypes.Type(value = TextContentPart.class, name = "text"),
})
```

### 2024-07-14

- Refactored model classes to accommodate fields accepting multiple types with custom serializers.
- Added Serializers util class to keep custom serializers in one place. Registered custom serializers in the Retrofit builder.
- Added OpenAI endpoints and models for Assistants.
- Added OpenAI endpoints and models for Threads.
- Added OpenAI endpoints and models for Messages.
- Added OpenAI endpoints and models for Runs.
- Added OpenAI endpoints and models for Run Step.

### 2024-07-15

- Finished modeling for OpenAI endpoints

### 2024-07-17

- Added support for Google Gemini.
- Added Google Gemini endpoints and models for Models.
- Finished modeling for Google Gemini endpoints `v1`.

May need to think about the naming strategy for models since different providers can have the same model names.

### 2024-07-18

- Added support for Ollama.
- Added Ollama endpoints and models.
- Overhauled Llmconfig, now it can take a default json config or an arbitrary json file path supplied by user.
- Refactored Merlin. Now it puts LlmService into a list.

### 2024-07-22

There are still some challenges to solve.

1. [ ] Add support for rate limiting, preferably integrate into service initialization.
2. [ ] Solve streaming, currently I have a unified `StreamingResponse<T>`, but it's not tested and need refinement.
3. [ ] Need to figure out how to handle backpressure when streaming. Backpressure is a term used to describe the situation where the remote is producing responses faster than the local can process. This can lead to memory leaks and other issues.
4. [ ] Google Gemini's documentation has been updated and introduced various new endpoints and dropped support for `v1`, need to update the endpoints completely.

Trying to complete the project for the next 2 week. Then start writing the dissertation.

In the future, I might change the modeling strategy for LLMs because currently the models are too cumbersome to maintain and don't support for parallel calls. But this is beyond the scope of the dissertation because it requires an overhaul of the models. I can provider ways to initiate parallel calls but the code will be cumbersome and far from elegant.

- [ ] provide examples later.
