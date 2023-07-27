package com.vs.foosh.api.exceptions.variable;

import java.util.UUID;

public class VariablePatchException extends RuntimeException {
    private UUID id;

    public VariablePatchException(UUID id) {
        super("An error occurred while processing your JSON Patch request. Make sure that the request is in the correct format (https://www.rfc-editor.org/rfc/rfc6902).");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }

}
