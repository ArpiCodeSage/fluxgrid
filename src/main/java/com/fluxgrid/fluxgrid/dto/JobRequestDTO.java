package com.fluxgrid.fluxgrid.dto;

import com.fluxgrid.fluxgrid.entity.JobType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record JobRequestDTO (
    @NotBlank @Size(max=255) String name,
    @NotNull JobType type,
    @Size(max=100_100) String payload,
    @Min(0) @Max(10) Integer priority
    
)
{}