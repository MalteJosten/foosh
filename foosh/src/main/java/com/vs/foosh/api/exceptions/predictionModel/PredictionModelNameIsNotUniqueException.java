package com.vs.foosh.api.exceptions.predictionModel;

import java.util.UUID;

public class PredictionModelNameIsNotUniqueException extends RuntimeException {
    private UUID id;

    public PredictionModelNameIsNotUniqueException(UUID id, String name) {
        super("The name '" + name + "' is already used!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
