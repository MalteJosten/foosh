package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatus;

public class FooSHJsonPatchIllegalArgumentException extends FooSHJsonPatchException {
    public FooSHJsonPatchIllegalArgumentException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
