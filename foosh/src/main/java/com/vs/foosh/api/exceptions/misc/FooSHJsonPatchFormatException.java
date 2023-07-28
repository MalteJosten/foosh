package com.vs.foosh.api.exceptions.misc;

public class FooSHJsonPatchFormatException extends RuntimeException {
    public FooSHJsonPatchFormatException() {
        super("The Patch document does not conform to the RFC 6902 standard! Please refer to https://www.rfc-editor.org/rfc/rfc6902");
    }
}
