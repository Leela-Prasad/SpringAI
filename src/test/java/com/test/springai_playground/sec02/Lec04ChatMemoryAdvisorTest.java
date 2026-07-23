package com.test.springai_playground.sec02;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;

public class Lec04ChatMemoryAdvisorTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec04ChatMemoryAdvisorTest.class);

    @BeforeAll
    public void setup() {
        var chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();
        var chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        client = builder
                .defaultAdvisors(spec -> spec.advisors(chatMemoryAdvisor)
                        .param(ChatMemory.CONVERSATION_ID, "default"))
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "What is the value of 3 * 4?",
            "what about 5 and 6?",
            "what if we divide that by 3?"
    })
    public void chatMemoryTest(String userMessage) {
        var assistantMessage = client.prompt(userMessage)
                .call()
                .content();

        log.info("Q: {}", userMessage);
        log.info("A: {}", assistantMessage);
    }
}
