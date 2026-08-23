package com.vinsguru.hiring.service;

import com.vinsguru.hiring.client.HiringAdvisorClient;
import com.vinsguru.hiring.client.JobClient;
import com.vinsguru.hiring.dto.JobApplicationSubmittedEvent;
import com.vinsguru.hiring.mapper.EntityDtoMapper;
import com.vinsguru.hiring.repoistory.JobApplicationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class JobApplicationEvaluationListener {

    private final JobApplicationRepository repository;
    private final JobClient jobClient;
    private final HiringAdvisorClient advisorClient;

    public JobApplicationEvaluationListener(JobApplicationRepository repository, JobClient jobClient, HiringAdvisorClient advisorClient) {
        this.repository = repository;
        this.jobClient = jobClient;
        this.advisorClient = advisorClient;
    }

    @Async
    @TransactionalEventListener
    public void handle(JobApplicationSubmittedEvent event) {
        var jobApplication = repository.findById(event.applicationId()).orElseThrow();
        var jobDetails = jobClient.getJobDetails(jobApplication.getJobId());
        var applicationEvaluationRequest = EntityDtoMapper.toJobApplicationEvaluationRequest(jobApplication, jobDetails);

        var response = advisorClient.evaluate(applicationEvaluationRequest);
        jobApplication.setMatchScore(response.matchScore());
        jobApplication.setMatchReasoning(response.matchReasoning());

        repository.save(jobApplication);
    }

}
