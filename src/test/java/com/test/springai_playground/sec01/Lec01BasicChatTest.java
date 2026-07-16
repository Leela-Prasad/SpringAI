package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01BasicChatTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01BasicChatTest.class);

    @Test
    public void basicChat(){
        var response = client.prompt("What is the capital of India")
                .call()
                .content();

        log.info(response);

        response = client.prompt("What is the population in that country")
                .call()
                .content();

        log.info(response);
    }
}
