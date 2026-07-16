package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public class Lec07StructuredOutput extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec07StructuredOutput.class);

    record Book(String title,
                String author,
                int publishedYear) {}

    record Books(List<Book> books) {}

    @Test
    public void mapEntityFromText() {
        var prompt = """
                Extract book details from the below text
                
                I just finished reading Clean code by John
                The copyright page says it was printed in 2015
                """;

        var book = executePrompt(prompt)
                .entity(Book.class);

        log.info("{}", book);
    }

    @Test
    public void mapEntitiesFromText() {
        var prompt = """
                Extract book details from the below text.
                
                Sam published a book on Java Programming in 2016.
                He also released another book on Python Programming, 3 years later.
                That same year Mike released a book on Reactive Programming.
                """;

//        OpenAI currently supports object as the root node,
//        arrays are not supported as the root node

//        var books = executePrompt(prompt)
//                .entity(new ParameterizedTypeReference<List<Book>>() {});

//        books.forEach(book -> log.info("{}", book));

        var response = executePrompt(prompt)
                .entity(Books.class);

        response.books.forEach(book -> log.info("{}", book));
    }

    private ChatClient.CallResponseSpec executePrompt(String prompt) {
        return client.prompt(prompt)
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .call();
    }

}
