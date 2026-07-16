package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03ChatResponseTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03ChatResponseTest.class);

    @Test
    public void testChatResponse() {
        var response = client.prompt("2 + 3 = ?")
                .call()
                .chatResponse();

        log.info("response: {}", response);
        log.info("Requests Per Minute: {}", response.getMetadata().getRateLimit().getRequestsLimit());
    }
}
