package com.vs.foosh.api.exceptions;

import java.util.UUID;

public class DeviceIdNotFoundException extends RuntimeException {
    private String message;

    public DeviceIdNotFoundException(String id) {
        super();

        try {
            UUID uniqueId = UUID.fromString(id);
            this.message = "Could not find device with id '" + uniqueId + "'!";
        } catch (IllegalArgumentException e) {
            this.message = "Could not find device with queryName '" + id.toLowerCase() + "'!";
        }
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
