package com.vinsguru.advisor.client;

import com.vinsguru.advisor.dto.*;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

public class CareerAdvisorClient {

    private static final String CANDIDATE = "candidate";
    private static final String JOBS = "jobs";

    private final ChatClient chatClient;
    private final CareerAdvisorPrompts careerAdvisorPrompts;
    private final JsonMapper jsonMapper;

    public CareerAdvisorClient(ChatClient chatClient, CareerAdvisorPrompts careerAdvisorPrompts, JsonMapper jsonMapper) {
        this.chatClient = chatClient;
        this.careerAdvisorPrompts = careerAdvisorPrompts;
        this.jsonMapper = jsonMapper;
    }

    public JobEvaluationResults evaulateJobs(CandidateDetails candidate, List<JobSummary> jobs) {
        return chatClient.prompt()
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .system(careerAdvisorPrompts.evaluateJobs().system())
                .user(spec -> spec.text(careerAdvisorPrompts.evaluateJobs().user())
                        .param(CANDIDATE, toJsonString(candidate)) // Here objects will be written with tostring() method, hence we need to write as json string
                        .param(JOBS, toJsonString(jobs)))
                .call()
                .entity(JobEvaluationResults.class);
    }

    public JobsComparisonResult compareJobs(CandidateDetails candidate, List<JobDetails> jobs) {
        return chatClient.prompt()
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .system(careerAdvisorPrompts.compareJobs().system())
                .user(spec -> spec.text(careerAdvisorPrompts.compareJobs().user())
                        .param(CANDIDATE, toJsonString(candidate))
                        .param(JOBS, toJsonString(jobs)))
                .call()
                .entity(JobsComparisonResult.class);
    }

    private String toJsonString(Object obj) {
        return jsonMapper.writeValueAsString(obj);
    }

    public TailoredResume generateReume(CandidateDetails candidate, JobDetails job) {
        var resume = chatClient.prompt()
                .system(careerAdvisorPrompts.generateResume().system())
                .user(spec -> spec.text(careerAdvisorPrompts.generateResume().user())
                        .param(CANDIDATE, toJsonString(candidate))
                        .param("job", toJsonString(job)))
                .call()
                .content();

        return new TailoredResume(job.id(), candidate.id(), resume);
    }
}
