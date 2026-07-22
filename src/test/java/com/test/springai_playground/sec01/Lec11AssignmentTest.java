package com.test.springai_playground.sec01;

import com.test.springai_playground.AbstractTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.AdvisorParams;

import java.util.List;

public class Lec11AssignmentTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec11AssignmentTest.class);

    record Movie(String title, float rating, int year) {
    }

    record Movies(List<Movie> movies) {
    }

    @BeforeAll
    public void setup() {
        client = builder.defaultSystem("""
                        You are a helpful Movie Recommender assistant.
                        
                        Rules:
                        - Suggest only well known movies.
                        - Recommend 3 similar movies
                        """)
                .defaultUser("Movie Title: {title}")
                .defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Narcos"
    })
    public void recommendMovies(String title) {
        executePrompt(title)
                .movies.forEach(movie -> log.info("{}", movie));
        
    }

    private Movies executePrompt(String title) {
        return client.prompt()
                .user(template -> template.param("title", title))
                .call()
                .entity(Movies.class);
    }
}
