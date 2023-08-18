package com.vs.foosh.api.model.variable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.variable.VariableNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.model.misc.IThingList;
import com.vs.foosh.api.model.misc.ModificationType;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilderService;

public class VariableList implements Serializable, IThingList<Variable, VariableDisplayRepresentation> {
    private List<Variable> variables;
    
    public VariableList() {
        this.variables = new ArrayList<>();
    }

    public void setList(List<Variable> variableList) {
        if (variables != null) {
            clearList();
        }

        this.variables.addAll(variableList);
    }

    public List<Variable> getList() {
        return this.variables;
    }

    public List<Thing> getAsThings() {
        List<Thing> things = new ArrayList<>();

        for(Variable variable: this.variables) {
            things.add(variable);
        }

        return things;
    }

    public void clearList() {
        for(Variable variable: this.variables) {
            variable.unregisterFromSubject();
            variable.notifyObservers(new VariableModification(ModificationType.DELETION, variable.getId()));
        }

        this.variables.clear();
    }

    public List<VariableDisplayRepresentation> getDisplayListRepresentation() {
        List<VariableDisplayRepresentation> displayRepresentation = new ArrayList<>();

        for(Variable variable: getList()) {
            displayRepresentation.add(new VariableDisplayRepresentation(variable));
        }

        return displayRepresentation;
    }

    public Variable getThing(String identifier) {
        for (Variable variable: getList()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equalsIgnoreCase(identifier)) {
                return variable;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    public void addThing(Variable variable) {
        this.variables.add(variable);
    }

    public void deleteThing(String identifier) {
        for (Variable variable: getList()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equalsIgnoreCase(identifier)) {
                variable.notifyObservers(new VariableModification(ModificationType.DELETION, variable.getId()));
                getList().remove(variable);
                return;
            }
        }

        throw new VariableNotFoundException(identifier);
    }
    
    /**
     * Return {@code true} if {@code variables} is {@code null} or empty.
     * 
     * @return {@code true} if {@code variables} is {@code null} or empty.
     */
    public boolean isListEmpty() {
        return (variables == null || variables.isEmpty());
    }

    public boolean isValidName(String name, UUID id) {
        try {
            // Check whether the provided 'name' could be an UUID.
            // Names in form of an UUID are disallowed.
            UUID.fromString(name);
            throw new VariableNameMustNotBeAnUuidException(id);
        } catch (IllegalArgumentException e) {
            for (Variable variable: this.variables) {
                // Check whether the name is already used
                if (variable.getName().equalsIgnoreCase(name)) {
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
        for (Variable variable: getList()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equals(identifier.toLowerCase())) {
                return;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    public List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilderService.getVariableListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilderService.getVariableListLink(), HttpAction.POST, List.of("application/json"));
        LinkEntry delete = new LinkEntry(label, LinkBuilderService.getVariableListLink(), HttpAction.DELETE, List.of());

        if (getList().isEmpty() || getList().size() == 0) {
            return new ArrayList<>(List.of(get, post));
        } else {
            return new ArrayList<>(List.of(get, delete));
        }
    }
    
    public void updateLinks() {
        for(Variable variable: getList()) {
            variable.updateLinks();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableList >>\n");
        builder.append("Variables: " + variables);

        return builder.toString();
    }

}
