package com.vs.foosh.api.exceptions.variable;

import org.springframework.http.HttpStatus;

public class VariableDevicePostException extends RuntimeException {
    private String variableId;
    private HttpStatus status;
   
    public VariableDevicePostException(String variableId, String message, HttpStatus status) {
        super(message);
        this.variableId = variableId;
        this.status     = status;
    }

    public String getVariableId() {
        return this.variableId;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

}
