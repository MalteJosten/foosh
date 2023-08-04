package com.vs.foosh.api.exceptions.predictionModel;

public class PredictionModelNotFoundException extends RuntimeException {
    private String id;

    public PredictionModelNotFoundException(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
    
}
