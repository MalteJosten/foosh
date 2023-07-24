package com.vs.foosh.api.model.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;

public class VariableList {
    private static List<Variable> variables;
    
    public static List<Variable> getInstance() {
        if (variables == null) {
            variables = new ArrayList<Variable>();
        }

        return variables;
    }

    public static void setVariables(List<Variable> variableList) {
        if (variables != null) {
            clearVariables();
        }

        getInstance().addAll(variableList);
    }

    public static void pushVariable(Variable variable) {
        // TODO: Pre-processing? Checks?
        getInstance().add(variable);
    }

    public static List<Variable> getVariables() {
        return getInstance();
    }

    public static List<VariableDisplayRepresentation> getDisplayListRepresentation() {
        List<VariableDisplayRepresentation> displayRepresentation = new ArrayList<>();

        for(Variable variable: getVariables()) {
            displayRepresentation.add(new VariableDisplayRepresentation(variable, variable.getSelfLinks()));
        }

        return displayRepresentation;
    }

    public static void clearVariables() {
        getInstance().clear();
    }

    public static Variable getVariable(String identifier) {
        for (Variable variable: getVariables()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equals(identifier.toLowerCase())) {
                return variable;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    public static boolean isUniqueName(String name, UUID id) {
        // Check whether the provided 'name' could be an UUID.
        // Names in form of an UUID are disallowed.
        try {
            UUID.fromString(name);
            throw new VariableNameMustNotBeAnUuidException(id);
        } catch (IllegalArgumentException e) {
            for (Variable variable: getInstance()) {
                // Check whether the name is already used
                if (variable.getName().equals(name)) {
                    // If it's already used, check whether it's the same variable.
                    if (variable.getId().equals(id)) {
                        return true;
                    }

                    return false;
                }
            
            }
        }
        
        return true;
    }

    public static void checkIfIdIsPresent(String identifier) {
        for (Variable variable: getVariables()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equals(identifier.toLowerCase())) {
                return;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    public static void deleteVariable(String id) {
        for (Variable variable: getVariables()) {
            if (variable.getId().toString().equals(id) || variable.getName().equals(id)) {
                getVariables().remove(variable);
                return;
            }
        }

        throw new VariableNotFoundException(id);
    }

    public static List<LinkEntry> getLinks(String label) {
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
    
    public static void updateVariableLinks() {
        for(Variable variable: getVariables()) {
            variable.updateLinks();
        }
    }
}
