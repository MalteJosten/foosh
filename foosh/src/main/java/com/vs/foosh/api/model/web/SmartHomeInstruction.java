package com.vs.foosh.api.model.web;

import java.net.URI;
import java.util.UUID;

/**
 * A {@link Record} containing a instruction which can be sent to and executed by the smart home API.
 * 
 * @param index the index of the instruction (it is used in {@link SmartHomePostResult} to link a response/{@link SmartHomePostResult} to the corresponding {@code SmartHomeInstruction})
 * @param deviceId the {@link UUID} of the {@link AbstractDevice} which this {@code SmartHomeInstruction} uses
 * @param payload the data which is send to the smart home API to change the device's state
 * @param deviceUri the {@link AbstractDevice}'s URI which is part of the smart home API and can be used to send the {@code payload} to
 */
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
