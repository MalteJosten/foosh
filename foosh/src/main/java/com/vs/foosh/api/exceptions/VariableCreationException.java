package com.vs.foosh.api.exceptions;

public class VariableCreationException extends RuntimeException {
    private String message;

    public VariableCreationException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
    
}
