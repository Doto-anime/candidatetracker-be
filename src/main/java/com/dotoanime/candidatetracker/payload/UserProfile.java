package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import java.time.Instant;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Instant createdAt;
    private Long jobCount;

    public UserProfile(Long id, String username, String name, Instant createdAt, Long jobCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.createdAt = createdAt;
        this.jobCount = jobCount;
    }
}
