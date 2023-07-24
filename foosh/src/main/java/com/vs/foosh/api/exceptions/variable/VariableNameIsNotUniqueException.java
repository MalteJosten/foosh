package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

public class VariableNameIsNotUniqueException extends RuntimeException {
    private UUID id;

    public VariableNameIsNotUniqueException(UUID id, String name) {
        super("The name '" + name + "' is already used!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
