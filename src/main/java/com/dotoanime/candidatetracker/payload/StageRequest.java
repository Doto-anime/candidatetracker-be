package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class StageRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String note;

//    private String stageStatus
}
