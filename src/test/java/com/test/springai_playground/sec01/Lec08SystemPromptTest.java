package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec08SystemPromptTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec08SystemPromptTest.class);

    @ParameterizedTest
    @ValueSource(strings = {
            "You are a physics teacher. Answer briefly",
            "You are a sound engineer. Answer briefly",
            "You are a finance expert. Answer briefly"
    })
    public void domainBasedResponses(String systemPrompt) {
        executePrompt(systemPrompt, "what is volume?");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "You are a sarcastic math teacher",
            "You are a helpful math teacher"
    })
    public void toneBasedResponses(String systemPrompt) {
        executePrompt(systemPrompt, "What is the square root of 100?");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "You are a helpful assistant. Answer in one word",
            "You are a helpful assistant. Answer in one sentence"
    })
    public void responseConstraints(String systemPrompt) {
        executePrompt(systemPrompt, "Explain Java");
    }

    private void executePrompt(String systemPrompt, String userPrompt) {
        var response = client.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

        log.info("{}", response);
    }
}
