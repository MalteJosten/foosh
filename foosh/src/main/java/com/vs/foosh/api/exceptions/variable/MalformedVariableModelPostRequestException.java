package com.vs.foosh.api.exceptions.variable;

public class MalformedVariableModelPostRequestException extends RuntimeException {
    private String id;

    public MalformedVariableModelPostRequestException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
    
}
