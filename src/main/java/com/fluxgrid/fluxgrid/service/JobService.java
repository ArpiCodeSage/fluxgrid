package com.fluxgrid.fluxgrid.service;

import com.fluxgrid.fluxgrid.dto.JobRequestDTO;
import com.fluxgrid.fluxgrid.dto.JobResponseDTO;
import com.fluxgrid.fluxgrid.dto.PageResponse;
import com.fluxgrid.fluxgrid.entity.Job;
import com.fluxgrid.fluxgrid.entity.JobStatus;
import com.fluxgrid.fluxgrid.exception.JobNotCancellableException;
import com.fluxgrid.fluxgrid.exception.JobNotFoundException;
import com.fluxgrid.fluxgrid.repository.JobRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {

    private static final int MAX_PAGE_SIZE = 100;

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
        Job saved = jobRepository.save(job); //returns the persisted entity instance
        return JobResponseDTO.from(saved);
    }

    @Transactional(readOnly = true) //tells that the method will only fetch data and not modify anything in the database
    public JobResponseDTO getJob(UUID id) {
        return jobRepository.findById(id)
                .map(JobResponseDTO::from)
                .orElseThrow(() -> new JobNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<JobResponseDTO> listJobs(JobStatus status, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Job> result = (status == null)
                ? jobRepository.findAll(pageable)
                : jobRepository.findByStatus(status, pageable);

        return new PageResponse<>(
                result.getContent().stream().map(JobResponseDTO::from).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Transactional
    public JobResponseDTO cancelJob(UUID id) {
        int updated = jobRepository.cancelIfPending(
                id, JobStatus.PENDING, JobStatus.CANCELLED, Instant.now());

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));

        if (updated == 0) {
            throw new JobNotCancellableException(id, job.getStatus());
        }
        return JobResponseDTO.from(job);
    }
}