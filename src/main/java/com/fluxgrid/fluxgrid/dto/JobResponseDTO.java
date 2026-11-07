package com.fluxgrid.fluxgrid.dto;

import com.fluxgrid.fluxgrid.entity.Job;
import com.fluxgrid.fluxgrid.entity.JobStatus;
import com.fluxgrid.fluxgrid.entity.JobType;
import java.time.Instant;
import java.util.UUID;

public record JobResponseDTO(
        UUID id,
        String name,
        JobType type,
        JobStatus status,
        String payload,
        int priority,
        int attempts,
        int maxRetries,
        String lastError,
        Instant createdAt,
        Instant updatedAt,
        Instant startedAt,
        Instant completedAt) {

    public static JobResponseDTO from(Job job) {
        return new JobResponseDTO(
                job.getId(),
                job.getName(),
                job.getType(),
                job.getStatus(),
                job.getPayload(),
                job.getPriority(),
                job.getAttempts(),
                job.getMaxRetries(),
                job.getLastError(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getStartedAt(),
                job.getCompletedAt());
    }
}