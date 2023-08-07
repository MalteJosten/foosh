package com.vs.foosh.api.model.variable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

public class VariableDisplayRepresentation {
    private VariableResponseObject variable;
    private List<LinkEntry> links;

    public VariableDisplayRepresentation(Variable variable) {
        this.variable = new VariableResponseObject(variable);
        this.links    = variable.getSelfLinks();
    }

    public VariableResponseObject getVariable() {
        return this.variable;
    }

    @JsonProperty("_links")
    public List<LinkEntry> getLinks() {
        return this.links;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableDisplayRepresentation >>\n");
        builder.append("Variable: " + variable + "\n");
        builder.append("Links: " + links);

        return builder.toString();
    }
}
