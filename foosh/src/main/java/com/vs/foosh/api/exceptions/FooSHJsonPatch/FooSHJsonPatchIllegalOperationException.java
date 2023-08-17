package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatus;

import com.vs.foosh.api.model.web.FooSHPatchOperation;

public class FooSHJsonPatchIllegalOperationException extends FooSHJsonPatchException {
    public FooSHJsonPatchIllegalOperationException(FooSHPatchOperation operation) {
        super(HttpStatus.BAD_REQUEST, "The operation '" + operation.toString().toLowerCase() + "' is not allowed on this resource!");
    }

    public FooSHJsonPatchIllegalOperationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
