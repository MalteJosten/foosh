package com.vs.foosh.api.model.variable;

import java.util.List;

import com.vs.foosh.api.model.web.LinkEntry;

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
