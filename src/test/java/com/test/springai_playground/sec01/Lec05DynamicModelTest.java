package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.ChatOptions;

public class Lec05DynamicModelTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec05DynamicModelTest.class);

    @ParameterizedTest
    @ValueSource(strings = {"gpt-4o-mini", "gpt-4.1-nano"})
    public void chatOptionsTest3(String model) {
        String prompt = "If 3 pencils cost $1.20, how much do 7 pencils cost? Return only the number";
        var builder = ChatOptions.builder()
                .model(model);

        runPrompt(prompt, builder);
    }

    private void runPrompt(String prompt, ChatOptions.Builder builder) {
        var response = client.prompt(prompt)
                .options(builder)
                .call()
                .content();

        log.info("Response: {}", response);
    }
}
