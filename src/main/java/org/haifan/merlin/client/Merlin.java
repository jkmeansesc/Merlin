package org.haifan.merlin.client;

import org.haifan.merlin.service.OpenAiService;

public class Merlin {

    private Provider currentProvider;

    public enum Provider {
        OPENAI, GOOGLE_GEMINI
    }

    public Merlin useProvider(Provider provider) {
        this.currentProvider = provider;
        return this;
    }

    OpenAiService service = new OpenAiService("https://api.openai.com", "sk-proj-5QxGWn88cH0D0flBcLGYT3BlbkFJQmFHZW5sshW08Wwf4um8");
}
