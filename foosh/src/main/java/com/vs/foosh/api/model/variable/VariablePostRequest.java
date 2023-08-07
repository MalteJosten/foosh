package com.vs.foosh.api.model.variable;

public record VariablePostRequest(String name) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariablePostRequest >>\n");
        builder.append("Name: \t" + name);

        return builder.toString();
    }
}
