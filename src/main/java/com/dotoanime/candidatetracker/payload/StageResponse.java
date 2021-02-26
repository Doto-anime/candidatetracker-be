package com.dotoanime.candidatetracker.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class StageResponse {
    private long id;
    private String name;
    private String note;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date doneAt;
}
