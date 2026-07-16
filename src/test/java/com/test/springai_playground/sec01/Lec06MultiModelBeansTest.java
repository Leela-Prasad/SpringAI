package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

public class Lec06MultiModelBeansTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec05DynamicModelTest.class);

    @Autowired
    private ChatClient proModel;

    @Autowired
    private ChatClient liteModel;

    @Test
    public void multiModelTest() {
        String prompt = "If 3 pencils cost $1.20, how much do 7 pencils cost? Return only the number";

        log.info("===Pro Model===");
        executePrompt(proModel, prompt);

        log.info("===Lite Model===");
        executePrompt(liteModel, prompt);
    }

    private void executePrompt(ChatClient client, String prompt) {
        var response = client.prompt(prompt)
                .call()
                .content();

        log.info("response={}", response);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ChatClient proModel(ChatClient.Builder builder) {
            return createClient(builder, "gpt-4o-mini");
        }

        @Bean
        public ChatClient liteModel(ChatClient.Builder builder) {
            return createClient(builder, "gpt-4.1-nano");
        }

        private ChatClient createClient(ChatClient.Builder builder, String modelName) {
            var chatOptionsBuilder = ChatOptions.builder()
                    .model(modelName);

            return builder.defaultOptions(chatOptionsBuilder)
                    .build();
        }

    }

}
