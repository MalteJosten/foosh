package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatus;

public class FooSHJsonPatchValueIsEmptyException extends FooSHJsonPatchException {
    public FooSHJsonPatchValueIsEmptyException() {
        super(HttpStatus.BAD_REQUEST, "The field 'value' needs to contain a value! Please refer to https://www.rfc-editor.org/rfc/rfc6902");
        super.name = "FooSHJsonPatchValueIsEmptyException";
    }
}
