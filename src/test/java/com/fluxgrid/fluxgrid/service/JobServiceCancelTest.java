package com.fluxgrid.fluxgrid.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fluxgrid.fluxgrid.dto.JobResponseDTO;
import com.fluxgrid.fluxgrid.entity.Job;
import com.fluxgrid.fluxgrid.entity.JobStatus;
import com.fluxgrid.fluxgrid.entity.JobType;
import com.fluxgrid.fluxgrid.exception.JobNotCancellableException;
import com.fluxgrid.fluxgrid.exception.JobNotFoundException;
import com.fluxgrid.fluxgrid.repository.JobRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobServiceCancelTest {

    @Mock
    JobRepository jobRepository;

    @InjectMocks
    JobService jobService;

    @Test
    void cancelJob_succeedsWhenOneRowUpdated() {
        UUID id = UUID.randomUUID();
        Job job = new Job("x", JobType.EMAIL, "{}");
        job.setStatus(JobStatus.CANCELLED); // what the database holds after the update

        when(jobRepository.cancelIfPending( //stubbing
                eq(id), eq(JobStatus.PENDING), eq(JobStatus.CANCELLED), any(Instant.class)))
                .thenReturn(1);
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        JobResponseDTO result = jobService.cancelJob(id);

        assertEquals(JobStatus.CANCELLED, result.status());
        verify(jobRepository).cancelIfPending(
                eq(id), eq(JobStatus.PENDING), eq(JobStatus.CANCELLED), any(Instant.class));
    }

    @Test
    void cancelJob_throwsConflictWhenJobIsNotPending() {
        UUID id = UUID.randomUUID();
        Job running = new Job("x", JobType.EMAIL, "{}");
        running.setStatus(JobStatus.RUNNING);

        when(jobRepository.cancelIfPending(
                eq(id), eq(JobStatus.PENDING), eq(JobStatus.CANCELLED), any(Instant.class)))
                .thenReturn(0);
        when(jobRepository.findById(id)).thenReturn(Optional.of(running));

        assertThrows(JobNotCancellableException.class, () -> jobService.cancelJob(id));
    }

    @Test
    void cancelJob_throwsNotFoundWhenJobDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(jobRepository.cancelIfPending(
                eq(id), eq(JobStatus.PENDING), eq(JobStatus.CANCELLED), any(Instant.class)))
                .thenReturn(0);
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class, () -> jobService.cancelJob(id));
    }

    @Test
    void getJob_throwsNotFoundWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class, () -> jobService.getJob(id));
    }
}