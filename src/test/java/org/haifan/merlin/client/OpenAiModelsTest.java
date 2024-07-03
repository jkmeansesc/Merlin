package org.haifan.merlin.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.TestCase;
import org.haifan.merlin.model.openai.models.Model;
import org.haifan.merlin.model.openai.OpenAiResponse;
import org.haifan.merlin.service.OpenAiService;
import org.haifan.merlin.utils.JsonPrinter;

public class OpenAiModelsTest extends TestCase {

    public void testOpenAiListModels() throws JsonProcessingException {
        OpenAiResponse<Model> merlinResponse = Merlin.<OpenAiService>builder()
                .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                .build()
                .getService()
                .listModels()
                .join();
        JsonPrinter.print(merlinResponse);
    }

    public void testOpenAiGetModel() throws JsonProcessingException {
        Model model = Merlin.<OpenAiService>builder()
                .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                .build()
                .getService()
                .retrieveModel("gpt-3.5-turbo-instruct")
                .join();
        JsonPrinter.print(model);
    }
}