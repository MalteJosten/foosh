package com.vs.foosh.api.exceptions.FooSHJsonPatch;

public class FooSHJsonPatchIllegalArgumentException extends RuntimeException {
    private String id;

    public FooSHJsonPatchIllegalArgumentException(String id, String message) {
        super(message);
        // System.out.println(operation);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
