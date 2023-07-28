package com.vs.foosh.api.exceptions.misc;

public class FooSHJsonPatchValueException extends RuntimeException {
    @SuppressWarnings("rawtypes")
    public FooSHJsonPatchValueException(Class valueClass) {
        super("Only values of type " + valueClass.getSimpleName() + " are allowed.");
    }
}
