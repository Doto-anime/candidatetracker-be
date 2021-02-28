package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class JobRequest {

    @NotBlank (message = "Company name must not be blank")
    private String companyName;

    @NotBlank (message = "Job position must not be blank")
    private String position;

    private String description;

    private String jobStatus;

    private List<StageRequest> stages;
}
