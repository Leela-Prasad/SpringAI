package com.test.springai_playground.sec02;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "logging.level.org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor=DEBUG"
})
public class Lec03SimpleLoggerAdvisorTest extends AbstractTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec03SimpleLoggerAdvisorTest.class);

    @Test
    public void loggingAdvisorTest() {
        var response = client.prompt("3 + 2 = ?")
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();

        logger.info("response: {}", response);
    }
}
