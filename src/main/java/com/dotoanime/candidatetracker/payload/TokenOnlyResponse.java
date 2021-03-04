package com.dotoanime.candidatetracker.payload;

import lombok.Data;

@Data
public class TokenOnlyResponse {
    private String token;
    private String tokenType = "Bearer";
    private Boolean success = true;
    private String message = "Token refresh success";

    public TokenOnlyResponse(String token) {
        this.token = token;
    }
}
