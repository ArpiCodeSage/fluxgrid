package com.fluxgrid.fluxgrid.repository;

import com.fluxgrid.fluxgrid.entity.Job;
import com.fluxgrid.fluxgrid.entity.JobStatus;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, UUID> {

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Job j SET j.status = :cancelled, j.updatedAt = :now "
            + "WHERE j.id = :id AND j.status = :pending") //Java Persistence Query Language(JPQL)
    int cancelIfPending(@Param("id") UUID id,
                        @Param("pending") JobStatus pending,
                        @Param("cancelled") JobStatus cancelled,
                        @Param("now") Instant now);
}//Spring Data JPA will implement these automatically.it will create an implementation of the interface at runtime.
