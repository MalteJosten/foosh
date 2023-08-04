package com.vs.foosh.api.exceptions.variable;

public class MalformedVariablePredictionRequest extends RuntimeException {
    private String id;

    public MalformedVariablePredictionRequest(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
    
}
