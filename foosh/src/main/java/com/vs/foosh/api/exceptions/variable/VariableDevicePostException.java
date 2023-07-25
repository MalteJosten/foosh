package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

import org.springframework.http.HttpStatus;

public class VariableDevicePostException extends RuntimeException {
    private String variableId;
    private UUID deviceUuid;
    private HttpStatus status;
   
    public VariableDevicePostException(String variableId, UUID deviceUuid, String message, HttpStatus status) {
        super(message);
        this.variableId = variableId;
        this.deviceUuid = deviceUuid;
        this.status     = status;
    }

    public String getVariableId() {
        return this.variableId;
    }

    public UUID getDeviceUuid() {
        return this.deviceUuid;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

}
