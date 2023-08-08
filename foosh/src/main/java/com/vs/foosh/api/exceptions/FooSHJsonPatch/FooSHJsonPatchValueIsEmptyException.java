package com.vs.foosh.api.exceptions.FooSHJsonPatch;

public class FooSHJsonPatchValueIsEmptyException extends RuntimeException {
    private String id;

    public FooSHJsonPatchValueIsEmptyException(String id) {
        super("The field 'value' needs to contain a value! Please refer to https://www.rfc-editor.org/rfc/rfc6902");
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
