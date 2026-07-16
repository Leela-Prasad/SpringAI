package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.Test;

public class Lec02ChatStreamTest extends AbstractTest {

    @Test
    public void testChatStream() {
        client.prompt("write a poem on java programming language")
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }
}
