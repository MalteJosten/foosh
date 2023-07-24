package com.vs.foosh.api.exceptions.variable;

public class VariableNamePatchRequest {
    private String id;
    private String name;

    public VariableNamePatchRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
