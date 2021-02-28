package com.dotoanime.candidatetracker.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ApiErrorResponse {
    private String message;
    private String path;
    private Boolean success = false;
    private Instant timeStamp;

    public ApiErrorResponse(String message, String path, Instant timeStamp) {
        this.message = message;
        this.path = path;
        this.timeStamp = timeStamp;
    }
}
