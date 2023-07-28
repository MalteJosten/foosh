package com.vs.foosh.api.model.web;

/// Conforms to Operations defined in [RFC 6902](https://www.rfc-editor.org/rfc/rfc6902#section-4)
public enum FooSHPatchOperation {
    ADD,
    REMOVE,
    REPLACE,
    MOVE,
    COPY,
    TEST
}
