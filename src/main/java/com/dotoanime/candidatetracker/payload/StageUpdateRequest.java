package com.dotoanime.candidatetracker.payload;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class StageUpdateRequest {

    private String name;

    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date doneAt;
}
