package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.haifan.merlin.api.GeminiApi;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.model.gemini.ModelList;
import org.haifan.merlin.utils.DefaultObjectMapper;
import org.haifan.merlin.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {
    @Mock
    private GeminiApi api;

    private GeminiService geminiService;

    private ObjectMapper mapper = DefaultObjectMapper.create();

    @Nested
    class V1Test {
        @Test
        void listModels() {
            geminiService = newMockedService();
            String expected = TestHelper.read("google_gemini/gemini_model_list.json");

            Call<ModelList> call = Mockito.mock(Call.class);
            when(api.listModels()).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, ModelList.class, mapper);

            ModelList response = Merlin.builder()
                    .addService(geminiService)
                    .build()
                    .getGeminiService()
                    .listModels()
                    .join();
            response.getModels().forEach(model -> assertNotNull(model.getName()));
        }
    }

    private GeminiService newMockedService() {
        return new GeminiService(api);
    }

    private GeminiService newRealService(GeminiService service) {
        return service;
    }
}