package com.dotoanime.candidatetracker.payload;

import com.dotoanime.candidatetracker.model.JobStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class JobResponse {
    private Long id;
    private String companyName;
    private String position;
    private String description;
    private JobStatus jobStatus;
    private List<StageResponse> stages;
    private UserSummary createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
