package com.vs.foosh.api.exceptions.FooSHJsonPatch;

public class FooSHJsonPatchFormatException extends RuntimeException {
    private String id;

    public FooSHJsonPatchFormatException(String id) {
        super("The Patch document does not conform to the RFC 6902 standard! Please refer to https://www.rfc-editor.org/rfc/rfc6902");
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
