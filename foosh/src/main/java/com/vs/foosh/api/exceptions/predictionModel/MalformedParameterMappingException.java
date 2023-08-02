package com.vs.foosh.api.exceptions.predictionModel;

public class MalformedParameterMappingException extends RuntimeException {
    private String id;

    public MalformedParameterMappingException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
