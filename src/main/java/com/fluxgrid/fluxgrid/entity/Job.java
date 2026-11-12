package com.fluxgrid.fluxgrid.entity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name="jobs")
public class Job {

    @Id 
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(nullable=false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private JobStatus status=JobStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private JobType type;

    @Column(columnDefinition="TEXT")
    private String payload;

    @Column(nullable=false,updatable=false)
    private Instant createdAt;

    private Instant startedAt;
    private Instant completedAt;
    
    @Column(nullable=false)
    private Instant updatedAt;

    @Column(nullable=false)
    private int maxRetries=3;

    @Column(nullable=false)
    private int priority=5;

    @Column(nullable=false)
    private int attempts=0;

    @Column(columnDefinition="TEXT")
    private String lastError;
    

    protected Job(){

    }

    public Job(String name,JobType type,String payload)
    {
        this.name=name;
        this.type=type;
        this.payload=payload;

    }

    @PrePersist
    public void onCreate(){
        Instant now=Instant.now();
        createdAt=now;
        updatedAt=now;
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt=Instant.now();
        }
    

    public UUID getId() {
        return id;
    }
      public String getName() {
        return name;
    }

    public JobType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public JobStatus getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getLastError() {
        return lastError;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
    
    public void setPriority(int priority)
    {
        if(priority<0 || priority>10)
        {
            throw new IllegalArgumentException("Priority must be between 0 and 10");
        }
        this.priority=priority;
    }

    public void setMaxRetries(int maxRetries)
    {
        if(maxRetries<0)
        {
            throw new IllegalArgumentException("maxRetries cannot be negative");

        }
        this.maxRetries=maxRetries;
    }
     public void setAttempts(int attempts) {
        if (attempts < 0) {
            throw new IllegalArgumentException(
                "attemptCount cannot be negative"
            );
        }
        this.attempts = attempts;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}

    

