package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

public class CouldNotFindUniqueDeviceNameException extends RuntimeException {
    private UUID id;
    private int timeoutCount;

    public CouldNotFindUniqueDeviceNameException(UUID id, int timeoutCount) {
        super("Could not find an unique name for device " + id + " after " + timeoutCount + " tries.");
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
