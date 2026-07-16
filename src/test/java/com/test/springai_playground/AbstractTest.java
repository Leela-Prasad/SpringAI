package com.test.springai_playground;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.profiles.active=testing"
})
// To avoid static setup method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractTest {

    @Autowired
    protected ChatClient.Builder builder;
    protected ChatClient client;

    @BeforeAll
    public void setup() {
        client = builder.build();
    }
}
