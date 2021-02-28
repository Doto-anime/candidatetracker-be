package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import java.util.List;

@Data
public class JobUpdateRequest {

    private String companyName;

    private String position;

    private String description;

    private String jobStatus;

    private List<StageRequest> stages;
}
