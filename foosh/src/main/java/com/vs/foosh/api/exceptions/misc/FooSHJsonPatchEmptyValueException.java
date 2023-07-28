package com.vs.foosh.api.exceptions.misc;

public class FooSHJsonPatchEmptyValueException extends RuntimeException {
    public FooSHJsonPatchEmptyValueException() {
        super("The value field needs to contain a value! Please refer to https://www.rfc-editor.org/rfc/rfc6902");
    }
}
