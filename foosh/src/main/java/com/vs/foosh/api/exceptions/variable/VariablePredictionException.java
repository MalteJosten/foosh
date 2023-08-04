package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

public class VariablePredictionException extends RuntimeException {
    private UUID id;

    public VariablePredictionException(UUID id, String message) {
        super(message);
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
    
}
