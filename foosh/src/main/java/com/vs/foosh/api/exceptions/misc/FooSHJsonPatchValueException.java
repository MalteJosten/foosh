package com.vs.foosh.api.exceptions.misc;

public class FooSHJsonPatchValueException extends RuntimeException {
    @SuppressWarnings("rawtypes")
    public FooSHJsonPatchValueException(Class valueClass) {
        super("This resources only allows values of type " + valueClass.getSimpleName() + ".");
    }
}
