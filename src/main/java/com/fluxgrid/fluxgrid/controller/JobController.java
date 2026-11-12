package com.fluxgrid.fluxgrid.controller;

import com.fluxgrid.fluxgrid.dto.JobRequestDTO;
import com.fluxgrid.fluxgrid.dto.JobResponseDTO;
import com.fluxgrid.fluxgrid.dto.PageResponse;
import com.fluxgrid.fluxgrid.entity.JobStatus;
import com.fluxgrid.fluxgrid.service.JobService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO request) //deserialization 
    {
        JobResponseDTO created = jobService.createJob(request);
        return ResponseEntity
                .created(URI.create("/api/jobs/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public JobResponseDTO getJob(@PathVariable UUID id) {
        return jobService.getJob(id);
    }

    @GetMapping
    public PageResponse<JobResponseDTO> listJobs(
            @RequestParam(required = false) JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return jobService.listJobs(status, page, size);
    }

    @PostMapping("/{id}/cancel")
    public JobResponseDTO cancelJob(@PathVariable UUID id) {
        return jobService.cancelJob(id);
    }
}