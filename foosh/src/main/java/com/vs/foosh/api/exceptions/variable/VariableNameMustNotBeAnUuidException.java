package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

public class VariableNameMustNotBeAnUuidException extends RuntimeException {
    private UUID id;

    public VariableNameMustNotBeAnUuidException(UUID id) {
        super("The name must not be an UUID!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
    
}
