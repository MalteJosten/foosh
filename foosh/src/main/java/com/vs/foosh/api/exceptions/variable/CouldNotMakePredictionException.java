package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

public class CouldNotMakePredictionException extends RuntimeException {
    private UUID variableId;

    public CouldNotMakePredictionException(UUID id, String message) {
        super(message);
        this.variableId = id;
    }

    public UUID getVariableId() {
        return this.variableId;
    }
}
