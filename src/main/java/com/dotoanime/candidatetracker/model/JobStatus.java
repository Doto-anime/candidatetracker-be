package com.dotoanime.candidatetracker.model;

import com.dotoanime.candidatetracker.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobStatus {
    ONGOING("ongoing"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String status;

    JobStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public static JobStatus fromString(String status) {
        for (JobStatus js : JobStatus.values()) {
            if (js.status.equalsIgnoreCase(status)) {
                return js;
            }
        }
        throw new BadRequestException("No job status constant with text" + status + " found");
    }
}
