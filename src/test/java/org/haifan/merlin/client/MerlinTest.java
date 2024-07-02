package org.haifan.merlin.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.TestCase;
import org.haifan.merlin.model.openai.models.Model;
import org.haifan.merlin.model.openai.OpenAiResponse;
import org.haifan.merlin.utils.JsonPrinter;

import java.util.concurrent.CompletableFuture;

public class MerlinTest extends TestCase {

    public void testMerlin() throws JsonProcessingException {
        Merlin merlin = new Merlin().useProvider(Merlin.Provider.OPENAI);
        CompletableFuture<OpenAiResponse<Model>> response = merlin.service.listModels();
        OpenAiResponse<Model> merlinResponse = response.join();
        JsonPrinter.print(merlinResponse);
    }

    public void testGetModel() throws JsonProcessingException {
        Merlin merlin = new Merlin().useProvider(Merlin.Provider.OPENAI);
        CompletableFuture<Model> response = merlin.service.getModel("gpt-3.5-turbo-instruct");
        Model model = response.join();
        JsonPrinter.print(model);
    }
}