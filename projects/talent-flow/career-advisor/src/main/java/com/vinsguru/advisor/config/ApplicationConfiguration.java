package com.vinsguru.advisor.config;

import com.vinsguru.advisor.client.CandidateClient;
import com.vinsguru.advisor.client.CareerAdvisorClient;
import com.vinsguru.advisor.client.JobClient;
import com.vinsguru.advisor.dto.CareerAdvisorPrompts;
import com.vinsguru.advisor.dto.PromptSet;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
public class ApplicationConfiguration {

    private static final String USER_TEMPLATE_PATH_FORMAT = "classpath:prompt-templates/%s/user.txt";
    private static final String SYSTEM_TEMPLATE_PATH_FORMAT = "classpath:prompt-templates/%s/system.txt";

    private final ResourceLoader resourceLoader;
    public ApplicationConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public CandidateClient candidateClient(RestClient.Builder builder, @Value("${candidate-service.url}") String baseUrl) {
        var client = builder.baseUrl(baseUrl).build();
        return new CandidateClient(client);
    }

    @Bean("compareJobsClient")
    public CareerAdvisorClient careerAdvisorClient(ChatClient.Builder builder, JsonMapper jsonMapper) {
        var client = builder.build();
        var promptSet = new CareerAdvisorPrompts(
                getPromptSet("compare-jobs"),
                getPromptSet("evaluate-jobs"),
                getPromptSet("generate-resume")
        );

        return new CareerAdvisorClient(client, promptSet, jsonMapper);
    }

    @Bean
    public JobClient jobClient(RestClient.Builder builder, @Value("${job-service.url}") String baseUrl) {
        var client = builder.baseUrl(baseUrl).build();
        return new JobClient(client);
    }

    private PromptSet getPromptSet(String feature) {
        return new PromptSet(
                getResourceContent(SYSTEM_TEMPLATE_PATH_FORMAT.formatted(feature)),
                getResourceContent(USER_TEMPLATE_PATH_FORMAT.formatted(feature))
        );
    }

    private String getResourceContent(String resourcePath) {
        try {
            return resourceLoader.getResource(resourcePath).getContentAsString(Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
