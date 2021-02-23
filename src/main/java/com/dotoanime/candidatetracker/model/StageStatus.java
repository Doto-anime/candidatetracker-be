package com.dotoanime.candidatetracker.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StageStatus {
    PASSED("passed"),
    REJECTED("rejected"),
    DECLINED("pending");

    private final String status;

    StageStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public static StageStatus fromString(String status) {
        for (StageStatus ts : StageStatus.values()) {
            if (ts.status.equalsIgnoreCase(status)) {
                return ts;
            }
        }
        throw new IllegalArgumentException("No transaction status constant with text " + status + " found");
    }
}
