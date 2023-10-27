package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatus;

public class FooSHJsonPatchFormatException extends FooSHJsonPatchException {
    public FooSHJsonPatchFormatException() {
        super(HttpStatus.BAD_REQUEST, "The Patch document does not conform to the RFC 6902 standard! Please refer to https://www.rfc-editor.org/rfc/rfc6902");
        super.name = "FooSHJsonPatchFormatException";
    }
}
