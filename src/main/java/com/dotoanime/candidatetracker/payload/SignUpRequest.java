package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {
    @NotBlank (message = "Name must not be blank")
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank (message = "Username must not be blank")
    @Size(min = 6, max = 25)
    private String username;

    @NotBlank (message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
