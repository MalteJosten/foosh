package com.vs.foosh.api.exceptions.predictionModel;

import java.util.UUID;

public class CouldNotFindVariableParameterMappingException extends RuntimeException {
    private UUID id;
    
    public CouldNotFindVariableParameterMappingException(UUID id, UUID variableId) {
        super("Could not find parameter mapping for variable " + variableId + "!");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
