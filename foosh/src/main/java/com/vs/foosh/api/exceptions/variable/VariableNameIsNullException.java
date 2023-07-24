package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

public class VariableNameIsNullException extends RuntimeException {
    private UUID id;

    public VariableNameIsNullException(UUID id) {
        super("The provided request body does not contain a field named 'name'!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }

}
