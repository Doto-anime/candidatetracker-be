package com.dotoanime.candidatetracker.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalAppException extends RuntimeException {
    public InternalAppException(String message) {
        super(message);
    }

    public InternalAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
