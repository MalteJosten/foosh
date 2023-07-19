package com.vs.foosh.api.exceptions;

public class VariableNotFoundException extends RuntimeException {
    private String id;

    public VariableNotFoundException(String id) {
        super("Could not find environmental variable with id '" + id + "'!");
        this.id = id;
    }

    public String getId() { return this.id; }
}
