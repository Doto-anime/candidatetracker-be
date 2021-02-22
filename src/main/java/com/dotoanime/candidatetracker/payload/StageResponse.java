package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import java.time.Instant;

@Data
public class StageResponse {
    private long id;
    private String name;
    private String note;
    private Instant createdAt;
}
