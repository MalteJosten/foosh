package com.vs.foosh.api.model;

import java.util.ArrayList;
import java.util.List;

import com.vs.foosh.api.exceptions.EnvironmentalVariableNotFoundException;
import com.vs.foosh.api.services.LinkBuilder;

public class EnvironmentVariableList {
    private static List<EnvironmentVariable> variables;
    
    public static List<EnvironmentVariable> getInstance() {
        if (variables == null) {
            variables = new ArrayList<EnvironmentVariable>();
        }

        return variables;
    }

    public static void setVariables(List<EnvironmentVariable> variableList) {
        if (variables != null) {
            clearVariables();
        }

        getInstance().addAll(variableList);
    }

    public void pushVariable(EnvironmentVariable variable) {
        // TODO: Pre-processing? Checks?
        getInstance().add(variable);
    }

    public static List<EnvironmentVariable> getVariables() {
        return getInstance();
    }

    public static void clearVariables() {
        getInstance().clear();
    }

    public static EnvironmentVariable getVariable(String id) {
        for (EnvironmentVariable variable: getVariables()) {
            if (variable.getId().toString().equals(id)) {
                return variable;
            }
        }

        throw new EnvironmentalVariableNotFoundException(id);
    }

    public static void deleteVariable(String id) {
        // TODO: Implement
    }

    public static List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.POST, List.of());
        LinkEntry put    = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.PUT, List.of());
        LinkEntry patch  = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.PATCH, List.of());
        LinkEntry delete = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.DELETE, List.of());

        if (getVariables().isEmpty() || getVariables().size() == 0) {
            return new ArrayList<>(List.of(get, put, post));
        } else {
            return new ArrayList<>(List.of(get, put, patch, delete));
        }
    }
}
