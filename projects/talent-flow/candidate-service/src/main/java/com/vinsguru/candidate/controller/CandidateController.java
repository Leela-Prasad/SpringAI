package com.vinsguru.candidate.controller;

import com.vinsguru.candidate.dto.CandidateDetails;
import com.vinsguru.candidate.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private static final Logger log = LoggerFactory.getLogger(CandidateController.class);
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<?> getCandidateById(@PathVariable Integer candidateId) {
        log.info("Fetching candidate by id: {}", candidateId);
        return candidateService.getCandidateDetails(candidateId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(Map.of()));
//        return this.candidateService.getCandidateDetails(candidateId);
    }
}
