package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

public class CouldNotFindUniqueQueryNameException extends RuntimeException {
    private UUID id;
    private int timeoutCount;

    public CouldNotFindUniqueQueryNameException(UUID id, int timeoutCount) {
        super("Could not find an unique queryName for device " + id + " after " + timeoutCount + " tries.");
        this.id = id;
        this.timeoutCount = timeoutCount;
    }

    public UUID getId() {
        return this. id;
    }

    public int getTimeoutCount() {
        return this.timeoutCount;
    }
}
