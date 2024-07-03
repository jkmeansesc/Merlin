package org.haifan.merlin.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.TestCase;
import org.haifan.merlin.model.openai.models.Model;
import org.haifan.merlin.model.openai.OpenAiResponse;
import org.haifan.merlin.service.OpenAiService;
import org.haifan.merlin.utils.JsonPrinter;

public class MerlinTest extends TestCase {

    public void testOpenAiListModels() throws JsonProcessingException {
        Merlin<OpenAiService> merlin = Merlin.<OpenAiService>builder()
                .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                .build();
        OpenAiResponse<Model> merlinResponse = merlin
                .getService()
                .listModels()
                .join();
        JsonPrinter.print(merlinResponse);
    }

    public void testOpenAiGetModel() throws JsonProcessingException {
        Merlin<OpenAiService> merlin = Merlin.<OpenAiService>builder()
                .service(new OpenAiService("sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8"))
                .build();
        Model model = merlin
                .getService()
                .getModel("gpt-3.5-turbo-instruct")
                .join();
        JsonPrinter.print(model);
    }
}