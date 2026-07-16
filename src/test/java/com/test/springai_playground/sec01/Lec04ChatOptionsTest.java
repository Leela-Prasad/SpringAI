package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.ChatOptions;

import java.util.stream.IntStream;

public class Lec04ChatOptionsTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec04ChatOptionsTest.class);

    @ParameterizedTest
    @ValueSource(ints = {5, 10})
    public void chatOptionsTest(int tokens) {
        String prompt = "3 + 4 = ?";
        var builder = ChatOptions
                .builder()
                .maxTokens(tokens);

        runPrompt(prompt, builder);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1.5})
    public void chatOptionsTest2(double temparature) {
        String prompt = "Suggest a name for my new coffee shop. Return only one word";
        var builder = ChatOptions
                .builder()
                .temperature(temparature);

        IntStream.rangeClosed(1, 3)
                .forEach(i -> runPrompt(prompt, builder));
    }

    private void runPrompt(String prompt, ChatOptions.Builder builder) {
        var response = client.prompt(prompt)
                .options(builder)
                .call()
                .chatResponse();

        log.info("Response: {}", response);
    }
}
