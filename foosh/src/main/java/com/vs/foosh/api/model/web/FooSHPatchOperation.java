package com.vs.foosh.api.model.web;

/**
 * An {@link enum} defining Json Patch Operations.
 * This conforms to <a href="https://www.rfc-editor.org/rfc/rfc6902#section-4">RFC 6902</a>.
 */
public enum FooSHPatchOperation {
    ADD,
    REMOVE,
    REPLACE,
    MOVE,
    COPY,
    TEST
}