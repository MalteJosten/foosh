package com.vs.foosh.api.model;

import java.util.List;

public class VariableDisplayRepresentation {
    private Variable variable;
    private List<LinkEntry> links;

    public VariableDisplayRepresentation(Variable variable, List<LinkEntry> links) {
        this.variable = variable;
        this.links    = links;
    }

    public Variable getVariable() {
        return this.variable;
    }

    public List<LinkEntry> getLinks() {
        return this.links;
    }
    
}
