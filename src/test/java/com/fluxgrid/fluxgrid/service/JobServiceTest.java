package com.fluxgrid.fluxgrid.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fluxgrid.fluxgrid.dto.JobRequestDTO;
import com.fluxgrid.fluxgrid.dto.JobResponseDTO;
import com.fluxgrid.fluxgrid.entity.Job;
import com.fluxgrid.fluxgrid.entity.JobStatus;
import com.fluxgrid.fluxgrid.entity.JobType;
import com.fluxgrid.fluxgrid.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    JobRepository jobRepository;

    @InjectMocks
    JobService jobService;

    @Test
    void createJob_usesDefaultPriorityWhenNull() {
        when(jobRepository.save(any(Job.class))).thenAnswer(inv -> inv.getArgument(0));

        JobResponseDTO result = jobService.createJob(
                new JobRequestDTO("send-report", JobType.REPORT, "{}", null));

        assertEquals(5, result.priority());
        assertEquals(JobStatus.PENDING, result.status());
    }

    @Test
    void createJob_appliesGivenPriority() {
        when(jobRepository.save(any(Job.class))).thenAnswer(inv -> inv.getArgument(0));

        JobResponseDTO result = jobService.createJob(
                new JobRequestDTO("send-report", JobType.REPORT, "{}", 9));

        assertEquals(9, result.priority());
    }
}