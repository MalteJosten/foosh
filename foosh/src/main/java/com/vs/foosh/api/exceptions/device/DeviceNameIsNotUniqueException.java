package com.vs.foosh.api.exceptions.device;

import java.util.UUID;

public class DeviceNameIsNotUniqueException extends RuntimeException {
    private UUID id;

    public DeviceNameIsNotUniqueException(UUID id, String name) {
        super("The name '" + name + "' is already used!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
