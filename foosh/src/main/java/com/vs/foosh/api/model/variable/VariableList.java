package com.vs.foosh.api.model.variable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.variable.VariableNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;

public class VariableList implements Serializable {
    private List<Variable> variables;
    
    public VariableList() {
        this.variables = new ArrayList<>();
    }

    public void setVariables(List<Variable> variableList) {
        if (variables != null) {
            clearVariables();
        }

        this.variables.addAll(variableList);
    }

    public void pushVariable(Variable variable) {
        this.variables.add(variable);
    }

    public List<Variable> getVariables() {
        return this.variables;
    }

    public List<VariableDisplayRepresentation> getDisplayListRepresentation() {
        List<VariableDisplayRepresentation> displayRepresentation = new ArrayList<>();

        for(Variable variable: getVariables()) {
            displayRepresentation.add(new VariableDisplayRepresentation(variable, variable.getSelfLinks()));
        }

        return displayRepresentation;
    }

    public void clearVariables() {
        this.variables.clear();
    }

    public Variable getVariable(String identifier) {
        for (Variable variable: getVariables()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equals(identifier.toLowerCase())) {
                return variable;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    public boolean isUniqueName(String name, UUID id) {
        try {
            // Check whether the provided 'name' could be an UUID.
            // Names in form of an UUID are disallowed.
            UUID.fromString(name);
            throw new VariableNameMustNotBeAnUuidException(id);
        } catch (IllegalArgumentException e) {
            for (Variable variable: this.variables) {
                // Check whether the name is already used
                if (variable.getName().equals(name)) {
                    // If it's already used, check whether it's the same variable.
                    if (variable.getId().equals(id)) {
                        return true;
                    }

                    throw new VariableNameIsNotUniqueException(id, name);
                }
            
            }
        }
        
        return true;
    }

    public void checkIfIdIsPresent(String identifier) {
        for (Variable variable: getVariables()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equals(identifier.toLowerCase())) {
                return;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    public void deleteVariable(String id) {
        for (Variable variable: getVariables()) {
            if (variable.getId().toString().equals(id) || variable.getName().equals(id)) {
                getVariables().remove(variable);
                return;
            }
        }

        throw new VariableNotFoundException(id);
    }

    public List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.POST, List.of("application/json"));
        LinkEntry patch  = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.PATCH, List.of("application/json"));
        LinkEntry delete = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.DELETE, List.of());

        if (getVariables().isEmpty() || getVariables().size() == 0) {
            return new ArrayList<>(List.of(get, post));
        } else {
            return new ArrayList<>(List.of(get, patch, delete));
        }
    }
    
    public void updateVariableLinks() {
        for(Variable variable: getVariables()) {
            variable.updateLinks();
        }
    }
}
