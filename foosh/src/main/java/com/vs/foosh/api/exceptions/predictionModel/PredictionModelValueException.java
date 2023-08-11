package com.vs.foosh.api.exceptions.predictionModel;

public class PredictionModelValueException extends RuntimeException {
    private String variableId;

    public PredictionModelValueException(String variableId, String message) {
        super(message);
        this.variableId = variableId;
    }

    public String getVariableId() {
        return this.variableId;
    }
    
}
