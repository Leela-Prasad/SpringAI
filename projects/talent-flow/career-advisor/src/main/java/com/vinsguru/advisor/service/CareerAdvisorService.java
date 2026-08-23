package com.vinsguru.advisor.service;

import com.vinsguru.advisor.client.CandidateClient;
import com.vinsguru.advisor.client.CareerAdvisorClient;
import com.vinsguru.advisor.client.JobClient;
import com.vinsguru.advisor.dto.JobEvaluationResult;
import com.vinsguru.advisor.dto.JobEvaluationResults;
import com.vinsguru.advisor.dto.JobsComparisonResult;
import com.vinsguru.advisor.dto.TailoredResume;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CareerAdvisorService {

    private final CandidateClient candidateClient;
    private final JobClient jobClient;
    private final CareerAdvisorClient careerAdvisorClient;

    public CareerAdvisorService(CandidateClient candidateClient, JobClient jobClient, CareerAdvisorClient careerAdvisorClient) {
        this.candidateClient = candidateClient;
        this.jobClient = jobClient;
        this.careerAdvisorClient = careerAdvisorClient;
    }

    public List<JobEvaluationResult> findJobs(int candidateId) {
        var candidate = candidateClient.getCandidateDetails(candidateId);
        var jobs = jobClient.searchBySkills(candidate.skills());
        return careerAdvisorClient.evaulateJobs(candidate, jobs)
                .result()
                .stream()
                .sorted(Comparator.comparingInt(JobEvaluationResult::matchScore).reversed())
                .toList();
    }

    public JobsComparisonResult compareJobs(int candidateId, List<Integer> jobIds) {
        var candidate = candidateClient.getCandidateDetails(candidateId);
        var jobs = jobClient.getJobsDetails(jobIds);
        return careerAdvisorClient.compareJobs(candidate, jobs);
    }

    public TailoredResume generateResume(int candidateId, int jobId) {
        var candidate = candidateClient.getCandidateDetails(candidateId);
        var job = jobClient.getJobDetails(jobId);
        return careerAdvisorClient.generateReume(candidate, job);
    }
}
