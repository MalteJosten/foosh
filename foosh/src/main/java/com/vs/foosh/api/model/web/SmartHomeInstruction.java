package com.vs.foosh.api.model.web;

import java.net.URI;
import java.util.UUID;

public class SmartHomeInstruction {
    private int index;
    private UUID deviceId;
    private String payload;
    private URI deviceUri;

    public SmartHomeInstruction(int index, UUID deviceId, String payload, URI uri) {
        this.index     = index;
        this.deviceId  = deviceId;
        this.payload   = payload;
        this.deviceUri = uri;
    }

    public int getIndex() {
        return this.index;
    }

    public UUID getDeviceId() {
        return this.deviceId;
    }

    public String getPayload() {
        return this.payload;
    }

    public URI getUri() {
        return this.deviceUri;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< SmartHomeInstruction >>\n");
        builder.append("Index:     " + index     + "\n");
        builder.append("Device-ID: " + deviceId  + "\n");
        builder.append("Payload:   " + payload   + "\n");
        builder.append("URI:       " + deviceUri + "\n");
    
        return builder.toString();
    }
}
