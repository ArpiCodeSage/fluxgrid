package com.fluxgrid.fluxgrid.service;

import com.fluxgrid.fluxgrid.dto.JobRequestDTO;
import com.fluxgrid.fluxgrid.dto.JobResponseDTO;
import com.fluxgrid.fluxgrid.entity.Job;
import com.fluxgrid.fluxgrid.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional
    public JobResponseDTO createJob(JobRequestDTO request) {
        Job job = new Job(request.name(), request.type(), request.payload());
        if (request.priority() != null) {
            job.setPriority(request.priority());
        }
        Job saved = jobRepository.save(job);
        return JobResponseDTO.from(saved);
    }
}