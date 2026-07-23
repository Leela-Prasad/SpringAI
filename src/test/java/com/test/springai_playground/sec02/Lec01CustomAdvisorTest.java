package com.test.springai_playground.sec02;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

public class Lec01CustomAdvisorTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01CustomAdvisorTest.class);

    @Test
    public void testLoggingAdvisor() {
        var response = client.prompt("3 + 2 = ?")
                .advisors(new LoggingAdvisor())
                .call()
                .content();

        log.info("Final Response: {}", response);
    }

    private static class LoggingAdvisor implements CallAdvisor {

        private static final Logger log = LoggerFactory.getLogger(Lec01CustomAdvisorTest.class);

        @Override
        public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
            // before call
            log.info("request: {}", request);

            // actual call
            var response = chain.nextCall(request);

            // after call
            log.info("response: {}", response);

            return response;
        }

        @Override
        public String getName() {
            return this.getClass().getName();
        }

        // Advisor executes available advisors in ascending order
        @Override
        public int getOrder() {
            return 100;
        }
    }
}
