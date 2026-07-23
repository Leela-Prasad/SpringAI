package com.test.springai_playground.sec02;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

import java.util.Optional;

public class Lec02AdvisorParamsTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec02AdvisorParamsTest.class);

    @BeforeAll
    public void setup() {
        client = builder.defaultAdvisors(new LoggingAdvisor())
                .build();
    }

    @ParameterizedTest
    @ValueSource(booleans = {
            true, false
    })
    public void testAdvisorParams(boolean logEnabled) {
        var response = client.prompt("What is 2 + 3 = ?")
                .advisors(spec -> spec.param("logEnabled", logEnabled))
                .call()
                .content();

        log.info("Final Response: {}", response);
    }


    private static class LoggingAdvisor implements CallAdvisor {

        private static final Logger log = LoggerFactory.getLogger(LoggingAdvisor.class);

        @Override
        public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
            var logEnabled = Optional.ofNullable(request.context().get("logEnabled"))
                    .map(Object::toString)
                    .map(Boolean::parseBoolean)
                    .orElse(false);

            // before call
            if (logEnabled)
                log.info("request: {}", request);

            // actual call
            var response = chain.nextCall(request);

            // after call
            if (logEnabled)
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
