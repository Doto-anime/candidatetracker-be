package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class JobRequest {

    @NotBlank
    @Size(max = 140)
    private String companyName;

    @NotBlank
    @Size(max = 140)
    private String position;

    private String description;

    @Valid
    private List<StageRequest> stages;
}
