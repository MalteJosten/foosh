package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

public class VariableNameIsEmptyException extends RuntimeException {
    private UUID id;

    public VariableNameIsEmptyException(UUID id) {
        super("The provided value for the field 'name' is empty!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }

}
