package com.fluxgrid.fluxgrid.exception;

import com.fluxgrid.fluxgrid.entity.JobStatus;
import java.util.UUID;

public class JobNotCancellableException extends RuntimeException {
    public JobNotCancellableException(UUID id, JobStatus current) {
        super("Job " + id + " cannot be cancelled because its status is " + current);
    }
}