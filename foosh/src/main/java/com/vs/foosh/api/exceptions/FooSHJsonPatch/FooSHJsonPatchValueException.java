package com.vs.foosh.api.exceptions.FooSHJsonPatch;

public class FooSHJsonPatchValueException extends RuntimeException {
    private String id;

    public FooSHJsonPatchValueException(String id) {
        super("An error occurred while processing the value field. Please make sure to provide the correct data type. Refer to the documentation for a list of valid data types.");
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
