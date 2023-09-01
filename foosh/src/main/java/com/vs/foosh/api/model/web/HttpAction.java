package com.vs.foosh.api.model.web;

/**
 * An {@link enum} defining HTTP methods.
 * It only includes operations defined by <a href="https://www.rfc-editor.org/rfc/rfc7231">HTTP/1.1</a>.
 */
public enum HttpAction {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE
}
