package com.vs.foosh.api.model.variable;

/**
 * A {@link Record} containing a request to create a new {@link Variable}, given a name.
 * 
 * @param name the name of the {@link Variable} in question
 */
public record VariablePostRequest(String name) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariablePostRequest >>\n");
        builder.append("Name: \t" + name);

        return builder.toString();
    }
}
