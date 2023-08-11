package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatus;

public class FooSHJsonPatchValueIsNullException extends FooSHJsonPatchException {
    public FooSHJsonPatchValueIsNullException() {
        super(HttpStatus.BAD_REQUEST, "There is no field called 'name'!");
    }
}
