package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatus;

public class FooSHJsonPatchValueException extends FooSHJsonPatchException {

    public FooSHJsonPatchValueException() {
        super(HttpStatus.BAD_REQUEST, "An error occurred while processing the value field. Please make sure to provide the correct data type. Refer to the documentation for a list of valid data types.");
        super.name = "FooSHJsonPatchValueException";
    }
}
