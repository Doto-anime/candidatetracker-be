package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class JobRequest {

    private String companyName;

    private String position;

    private String description;

    private String jobStatus;

    private List<StageRequest> stages;
}
