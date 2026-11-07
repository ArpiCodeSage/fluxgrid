package com.fluxgrid.fluxgrid.controller;
import com.fluxgrid.fluxgrid.dto.JobRequestDTO;
import com.fluxgrid.fluxgrid.dto.JobResponseDTO;
import com.fluxgrid.fluxgrid.service.JobService;
import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController 
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService) {
    this.jobService = jobService;
}
@PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO request) {
        JobResponseDTO created = jobService.createJob(request);
        return ResponseEntity
                .created(URI.create("/api/jobs/" + created.id()))
                .body(created);
    }
    
}
