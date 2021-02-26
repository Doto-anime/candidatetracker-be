package com.dotoanime.candidatetracker.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private String path;
    private Boolean success = false;
    private Instant timeStamp;

    public ApiError(String message, String path, Instant timeStamp) {
        this.message = message;
        this.path = path;
        this.timeStamp = timeStamp;
    }
}
