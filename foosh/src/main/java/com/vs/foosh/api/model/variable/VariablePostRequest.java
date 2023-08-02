package com.vs.foosh.api.model.variable;

// TODO: @Override toString()
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
        StringBuilder builder = new StringBuilder();
        builder.append("<< VariablePostRequest >>\n");
        builder.append("name: \t" + name + "\n");

        return builder.toString();
    }
}
