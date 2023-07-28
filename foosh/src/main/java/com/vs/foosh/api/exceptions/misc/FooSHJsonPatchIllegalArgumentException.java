package com.vs.foosh.api.exceptions.misc;

public class FooSHJsonPatchIllegalArgumentException extends RuntimeException {
    public FooSHJsonPatchIllegalArgumentException(String operation) {
        super("The operation " + operation + " is not a valid Json Patch operation. Please visit https://www.rfc-editor.org/rfc/rfc6902#section-4 for a list of valid operations.");
    }
}
