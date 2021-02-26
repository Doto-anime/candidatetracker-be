package com.dotoanime.candidatetracker.payload;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class StageRequest {
    @NotBlank
    private String name;

    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date doneAt;
//    private String stageStatus
}
