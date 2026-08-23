package com.vinsguru.hiring.service;


import com.vinsguru.hiring.client.JobClient;
import com.vinsguru.hiring.dto.*;
import com.vinsguru.hiring.entity.JobApplication;
import com.vinsguru.hiring.mapper.EntityDtoMapper;
import com.vinsguru.hiring.repoistory.JobApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobClient jobClient;
    private final JobApplicationRepository repository;
    private final ApplicationEventPublisher publisher;

    public JobApplicationService(JobClient jobClient, JobApplicationRepository repository, ApplicationEventPublisher publisher) {
        this.jobClient = jobClient;
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public void submitApplication(JobApplicationSubmissionRequest request) {

        var jobApplication = repository.findByJobIdAndCandidateId(request.jobId(), request.candidateId())
                .orElseGet(() -> EntityDtoMapper.toJobApplication(request));

        jobApplication.setAppliedDate(LocalDate.now());
        jobApplication.setResume(request.resume());

        jobApplication.setMatchScore(null);
        jobApplication.setMatchReasoning(null);

        repository.save(jobApplication);
        publisher.publishEvent(new JobApplicationSubmittedEvent(jobApplication.getId()));
    }

    public List<JobApplicationDetails> getApplicationsByJobId(Integer jobId) {
        return repository.findByJobId(jobId)
                .stream()
                .map(EntityDtoMapper::toJobApplicationDetails)
                .toList();
    }

    public List<CandidateApplication> getApplicationsByCandidateId(Integer candidateId) {

        List<JobApplication> jobApplications = repository.findByCandidateId(candidateId);

        var jobIds = jobApplications.stream()
                .map(JobApplication::getJobId)
                .toList();

        var jobDetails = jobClient.getJobsDetails(jobIds)
                .stream()
                .collect(Collectors.toMap(
                        JobDetails::id,
                        Function.identity()
                ));

        return jobApplications.stream()
                .map(jobApplication -> EntityDtoMapper.toCandidateJobApplication(jobApplication, jobDetails.get(jobApplication.getJobId())))
                .toList();
    }

}
