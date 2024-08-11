# Merlin

Merlin is a Java util library that provides an unified interface to interact with Large Language Models (LLMs).

| Libraries used                                                   |
| ---------------------------------------------------------------- |
| [okhttp](https://github.com/square/okhttp)                       |
| [log4j](https://logging.apache.org/log4j/2.x/index.html)         |
| [SLF4J](https://www.slf4j.org/index.html)                        |
| [jackson](https://github.com/FasterXML/jackson)                  |
| [lombok](https://projectlombok.org/)                             |
| [RxJava](https://github.com/ReactiveX/RxJava?tab=readme-ov-file) |
| [JUnit5](https://junit.org/junit5/)                              |

## Features

> OpenAI: deprecated and legacy features are not supported

> Google Gemini: currently supports `v1` endpoints.

TBA

## Getting Started

### Add Merlin to your project

#### Maven

```xml
<dependency>
    <groupId>io.github.jkmeansesc</groupId>
    <artifactId>merlin</artifactId>
    <version>0.1.0</version>
</dependency>
```

#### Gradle

```gradle
implementation group: 'io.github.jkmeansesc', name: 'merlin', version: '0.1.0'
```

#### Jar

Download the latest JAR file [here](dist/), and import it into your project manually.

### Initialize Merlin

Merlin utilizes builder pattern for a consistent and flexible initialization of all supported LLM services. By default, Merlin will look for API keys through environment variables and fall back to direct initialization with a supplied API key. If neither is found, an Exception will be thrown.

| Provider       | Environment Variable |
| -------------- | -------------------- |
| OpenAI ChatGPT | `OPENAI_KEY`         |
| Goole Gemini   | `GOOGLE_GEMINI_KEY`  |
| Ollama         | Not Applicable       |

You can initialize all services like this:

```java
        // default
        Merlin merlin = Merlin
                .builder()
                .openai()
                .gemini()
                .ollama()
                .build();

        // or with supplied token
        Merlin merlin = Merlin
                .builder()
                .openai("your-openai-token")
                .gemini("your-gemini-token")
                .ollama() // no token needed for ollama
                .build();

        // or with custom URL
        Merlin merlin = Merlin
                .builder()
                .openaiWith("https://custom.openai.base.url.if.you.use.relay.com")
                .geminiWith("https://custom.gemini.base.url.if.you.use.relay.com")
                .ollama("https://some.ollama.url.com")
                .build();

        // or with custom URL and token
        Merlin merlin = Merlin
                .builder()
                .openaiWith("https://some.openai.relay.com", "your-openai-token")
                .geminiWith("https://some.gemini.relay.com", "your-gemini-token")
                .ollama("https://some.ollama.url.com")
                .build();

        OpenAiService openAiService = merlin.getService(OpenAiService.class);
        GeminiService geminiService = merlin.getService(GeminiService.class);
        OllamaService ollamaService = merlin.getService(OllamaService.class);
```

If you only intend to use one service:

```java
        // other builder methods also available
        OpenAiService service = Merlin.builder().openai().build().getService(OpenAiService.class);
```

### Calling LLM endpoints

After initialization, you can use each service to call the respective LLM endpoints. Each service is designed to be as close to the original API as possible, with the addition of error handling and logging. Each non-streaming endpoints returns a `CompletableFuture` object, which can be used to handle responses asynchronously.

```java
        // OpenAI
        CompletableFuture<OpenAiData<Model>> openAiResponse = openAiService.listModels();
        // Google Gemini
        CompletableFuture<GeminiData<Model>> geminiResponse = geminiService.listModels();
        // Ollama
        CompletableFuture<OllamaModelList> ollamaResponse = ollamaService.listModels();
```

For streaming endpoints, you can use the `StreamingResponse<T>` object to handle responses incrementally.

```java
        List<OllamaMessage> messages = new ArrayList<>();

        OllamaMessage message = OllamaMessage.builder().role("user").content("Are you online?").build();
        messages.add(message);

        OllamaCompletionRequest request = OllamaCompletionRequest.builder().model("mistral").messages(messages).build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        ollamaService.streamChatCompletion(request).start(chunk -> { // handle each chunk here
            System.out.println("Received chunk: " + chunk.getMessage().getContent());
            receivedChunks.add(chunk.getMessage().getContent());
        }, error -> { // handle error
            System.err.println("Error occurred: " + error.getMessage());
            e.set(error);
            future.completeExceptionally(error);
        }, () -> { // things you want to do on complete
            System.out.println("Streaming completed");
            isDone.set(true);
            future.complete(null);
        });

        future.join();
```

The class `StreamingResponse<T>` uses RxJava's `Flowable` under the hood. If you are familiar with reactive programming, you can directly use the `Flowable` object to get access to the full potential of what RxJava provides:

```java
        ollamaService.streamChatCompletion(request).asFlowable().doYourThing();
```

### Customizing configurations

You can supply a custom config for each service by creating a `LlmConfig` object and pass it to the builder.

```java
        // set token to null will default to environment variable
        LlmConfig openaiConfig = new LlmConfig(Provider.OPENAI, OpenAiService.DEFAULT_BASE_URL, null);
        LlmConfig geminiConfig = new LlmConfig(Provider.GOOGLE_GEMINI, GeminiService.DEFAULT_BASE_URL, null);
        LlmConfig ollamaConfig = new LlmConfig(Provider.OLLAMA, OllamaService.DEFAULT_BASE_URL, null);

        Merlin merlin = Merlin
                .builder()
                .openai(openaiConfig)
                .gemini(geminiConfig)
                .ollama(ollamaConfig)
                .build();

        OpenAiService openAiService = merlin.getService(OpenAiService.class);
        GeminiService geminiService = merlin.getService(GeminiService.class);
        OllamaService ollamaService = merlin.getService(OllamaService.class);
```

After initialization, `LlmConfig` supports setting log level and default timeout, more configurations will be added in future releases.

```java
        openAiService.getConfig().setLogLevel(LlmConfig.Level.BODY);
        geminiService.getConfig().setTimeOut(Duration.ofSeconds(60));
```

- `setTimeOut()` accepts a `Duration` object from `java.time`.
- possible values for `LlmConfig.Level`:
  - `LlmConfig.Level.NONE`
  - `LlmConfig.Level.BASIC`
  - `LlmConfig.Level.HEADERS`
  - `LlmConfig.Level.BODY`

> When supplying your custom config, you are responsible for passing it to the correct service builder.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss what you would like to change.

## Additional Information

Merlin follows [Semantic Versioning](https://semver.org/).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
