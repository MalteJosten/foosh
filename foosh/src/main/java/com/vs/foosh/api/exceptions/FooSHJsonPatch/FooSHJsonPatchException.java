package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import org.springframework.http.HttpStatusCode;

public class FooSHJsonPatchException extends RuntimeException {
    protected HttpStatusCode statusCode;
    protected String name = "FooSHJsonPatchException";

    public FooSHJsonPatchException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return this.statusCode;
    }

    public String getName() {
        return this.name;
    }
    
}
