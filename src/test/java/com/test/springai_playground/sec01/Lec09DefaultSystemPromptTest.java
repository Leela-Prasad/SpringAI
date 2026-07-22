package com.test.springai_playground.sec01;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

public class Lec09DefaultSystemPromptTest {

    private static final Logger log = LoggerFactory.getLogger(Lec09DefaultSystemPromptTest.class);

    @Autowired
    private ChatClient chatClient;

    @Test
    public void test() {
        var response = chatClient.prompt("what is volume?")
                .call()
                .content();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ChatClient chatClient(ChatClient.Builder builder) {
            return builder.defaultSystem("""
                            You are a finance expert.
                            Answer only finance related questions.
                            keep responses under 2 sentences.
                            """)
                    .build();
        }

        @Bean
        public ChatClient chatClientSystemPromptFromFile(ChatClient.Builder builder, ResourceLoader resourceLoader) {
            return builder.defaultSystem(resourceLoader.getResource("classpath:system-instructions.txt"))
                    .build();
        }
    }
}
