package com.vs.foosh.api.model.web;

import java.net.URI;
import java.util.UUID;

public class SmartHomeInstruction {
    private UUID deviceId;
    private String value;
    private URI deviceUri;

    public SmartHomeInstruction(UUID id, String value, URI uri) {
        this.deviceId  = id;
        this.value     = value;
        this.deviceUri = uri;
    }

    public UUID getDeviceId() {
        return this.deviceId;
    }

    public String getValue() {
        return this.value;
    }

    public URI getUri() {
        return this.deviceUri;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< SmartHomeInstruction >>");
        builder.append("Device-ID:\t" + deviceId  + "\n");
        builder.append("Value:\t"     + value     + "\n");
        builder.append("URI:\t"       + deviceUri +  "\n");
    
        return builder.toString();
    }
}
