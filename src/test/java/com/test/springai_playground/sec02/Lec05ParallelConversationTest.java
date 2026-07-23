package com.test.springai_playground.sec02;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;

public class Lec05ParallelConversationTest extends AbstractTest {

    private final static Logger log = LoggerFactory.getLogger(Lec05ParallelConversationTest.class);

    @BeforeAll
    public void setup() {
        var chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();
        var chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        client = builder.defaultAdvisors(chatMemoryAdvisor).build();
    }

    @Test
    public void parallelConversations() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            chat("sam", "My Number is 10");
            sleep(9000);
            chat("sam", "Add 5 to it. What is the result?");
        });

        Thread t2 = new Thread(() -> {
            sleep(3000);
            chat("mike", "My Number is 20");
            sleep(3000);
            chat("mike", "Multiply it by 2. What is the result?");
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private void chat(String converstationId, String userMessage) {
        var assistantMessage = client.prompt(userMessage)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, converstationId))
                .call()
                .content();

        log.info("[{}] Q: {}", converstationId, userMessage);
        log.info("[{}] A: {}", converstationId, assistantMessage);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
