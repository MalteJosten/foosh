package com.vs.foosh.api.exceptions.predictionModel;

import java.util.UUID;

public class PredictionModelNameMustNotBeAnUuidException extends RuntimeException {
    private UUID id;

    public PredictionModelNameMustNotBeAnUuidException(UUID id) {
        super("The name must not be an UUID!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
    
}
