package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import java.util.UUID;

public class FooSHJsonPatchReplaceException extends RuntimeException {
    private UUID modelId;

    public FooSHJsonPatchReplaceException(UUID modelId, String message) {
        super(message);
        this.modelId = modelId;
    }

    public UUID getModelId() {
        return this.modelId;
    }
    
}
