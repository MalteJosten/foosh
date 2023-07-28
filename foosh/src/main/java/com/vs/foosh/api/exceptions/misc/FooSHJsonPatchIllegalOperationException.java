package com.vs.foosh.api.exceptions.misc;

import com.vs.foosh.api.model.web.FooSHPatchOperation;

public class FooSHJsonPatchIllegalOperationException extends RuntimeException {
    public FooSHJsonPatchIllegalOperationException(FooSHPatchOperation operation) {
        super("The operation " + operation.toString().toLowerCase() + " is not allowed on this resource!");
    }
}
