package com.fluxgrid.fluxgrid.repository;

import com.fluxgrid.fluxgrid.entity.Job;
import com.fluxgrid.fluxgrid.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByStatus(JobStatus status);
    List<Job> findByStatusOrderByPriorityDesc(JobStatus status);
    
}//Spring Data JPA will implement these automatically.it will create an implementation of the interface at runtime.
