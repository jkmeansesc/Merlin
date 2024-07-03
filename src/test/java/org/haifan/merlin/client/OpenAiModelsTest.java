package org.haifan.merlin.client;

import junit.framework.TestCase;
import org.haifan.merlin.service.OpenAiService;

public class OpenAiModelsTest extends TestCase {

    public void testOpenAiListModels() {
        Merlin.<OpenAiService>builder()
                .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                .build()
                .getService()
                .listModels()
                .join();
    }

    public void testOpenAiGetModel() {
        Merlin.<OpenAiService>builder()
                .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                .build()
                .getService()
                .retrieveModel("gpt-3.5-turbo-instruct")
                .join();
    }
}