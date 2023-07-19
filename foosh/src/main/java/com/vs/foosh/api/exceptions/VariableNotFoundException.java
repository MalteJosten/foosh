package com.vs.foosh.api.exceptions;

import java.util.UUID;

public class VariableNotFoundException extends RuntimeException {
    private String message;

    public VariableNotFoundException(String id) {
        super();

        try {
            UUID uniqueId = UUID.fromString(id);
            this.message = "Could not find variable with id '" + uniqueId + "'!";
        } catch (IllegalArgumentException e) {
            this.message = "Could not find variable with name '" + id.toLowerCase() + "'!";
        }
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
