package com.dotoanime.candidatetracker.payload;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class StageRequest {

    @NotBlank (message = "Stage name must not be blank")
    private String name;

    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date doneAt;
}
