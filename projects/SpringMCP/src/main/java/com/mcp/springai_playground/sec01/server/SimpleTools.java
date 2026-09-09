package com.mcp.springai_playground.sec01.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SimpleTools {

    private static final Logger loggger = LoggerFactory.getLogger(SimpleTools.class);

    @McpTool(name="generate-random-number", description = "Generates a random number")
    public Integer generateRandomNumber() {
        var random = ThreadLocalRandom.current().nextInt(1, 1000);
        loggger.info("Generated random number: {}", random);
        return random;
    }

    @McpTool(description = "save the text content to a file")
    public void saveContentToFile(String text) {
        loggger.info("Saving content: {}", text);
    }

}
