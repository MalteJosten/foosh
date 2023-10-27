package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatus;

public class FooSHJsonPatchIllegalPathException extends FooSHJsonPatchException {
    public FooSHJsonPatchIllegalPathException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
        super.name = "FooSHJsonPatchIllegalPathException";
    }

}
