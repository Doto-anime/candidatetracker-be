package com.dotoanime.candidatetracker.payload;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String username;
    private String name;
    private String token;
    private String tokenType = "Bearer";
    private Boolean success = true;

    public JwtAuthenticationResponse(String username, String name, String token) {
        this.username = username;
        this.name = name;
        this.token = token;
    }
}
