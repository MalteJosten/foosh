package com.vs.foosh.api.model.variable;

public class VariablePostRequest {
    private String name;
    
    public VariablePostRequest() {}

    public VariablePostRequest(String name) {
        this.name    = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariablePostRequest >>\n");
        builder.append("Name: \t" + name);

        return builder.toString();
    }
}
