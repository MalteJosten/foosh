package com.vs.foosh.api.model.web;

import java.net.URI;
import java.util.UUID;

public record SmartHomeInstruction(int index, UUID deviceId, String payload, URI deviceUri) {
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
