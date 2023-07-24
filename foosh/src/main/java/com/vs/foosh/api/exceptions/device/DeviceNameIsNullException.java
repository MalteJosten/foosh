package com.vs.foosh.api.exceptions.device;

import java.util.Map;
import java.util.UUID;

public class DeviceNameIsNullException extends RuntimeException {
    private UUID id;
   
    public DeviceNameIsNullException(UUID id, Map<String, String> requestBody) {
        super("The provided request body " + requestBody.toString() + " does not contain a field named 'queryName'!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
