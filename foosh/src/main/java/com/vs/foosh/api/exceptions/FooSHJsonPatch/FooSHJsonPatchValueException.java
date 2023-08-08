package com.vs.foosh.api.exceptions.FooSHJsonPatch;

public class FooSHJsonPatchValueException extends RuntimeException {
    private String id;

    @SuppressWarnings("rawtypes")
    public FooSHJsonPatchValueException(String id, Class valueClass) {
        super("This resources only allows values of type " + valueClass.getSimpleName() + ".");
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
