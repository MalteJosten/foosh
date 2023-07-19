package com.vs.foosh.api.model;

import java.util.ArrayList;
import java.util.List;

import com.vs.foosh.api.exceptions.VariableNotFoundException;
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

    public static void clearVariables() {
        getInstance().clear();
    }

    public static Variable getVariable(String id) {
        for (Variable variable: getVariables()) {
            if (variable.getId().toString().equals(id)) {
                return variable;
            }
        }

        throw new VariableNotFoundException(id);
    }

    public static boolean isUniqueName(String name) {
        if (variables == null || variables.isEmpty()) {
            return true;
        } else {
            for (Variable variable : variables) {
                if (variable.getName().equals(name.toLowerCase())) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void deleteVariable(String id) {
        // TODO: Implement
    }

    public static List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.POST, List.of("application/json"));
        LinkEntry put    = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.PUT, List.of("application/json"));
        LinkEntry patch  = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.PATCH, List.of("application/json"));
        LinkEntry delete = new LinkEntry(label, LinkBuilder.getVariableListLink(), HttpAction.DELETE, List.of());

        if (getVariables().isEmpty() || getVariables().size() == 0) {
            return new ArrayList<>(List.of(get, put, post));
        } else {
            return new ArrayList<>(List.of(get, put, patch, delete));
        }
    }
}
