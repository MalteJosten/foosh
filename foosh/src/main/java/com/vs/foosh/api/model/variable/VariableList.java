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
import com.vs.foosh.api.services.helpers.LinkBuilderService;

/**
 * A container holding all currently registered {@link Variable}s with a bunch of functions allowing setting, retrieval, and modification of
 * the managed variables.
 * 
 * It implements {@link IThingList}<{@link Variable}, {@link VariableDisplayRepresentation}> to give the class capabilities of a list of {@link Thing}s.
 * It implements {@link Serializable} so that it can be (de)serialized for saving and loading into and from persistent storage.
 * 
 * @see com.vs.foosh.api.services.PersistentDataService#saveVariableList()
 * @see com.vs.foosh.api.services.PersistentDataService#hasSavedVariableList()
 */
public class VariableList implements Serializable, IThingList<Variable, VariableDisplayRepresentation> {
    /**
     * The list containing all registered {@link Variable}s.
     */
    private List<Variable> variables;
    
    /**
     * Create a {@code VariableList} with no variables.
     */
    public VariableList() {
        this.variables = new ArrayList<>();
    }

    /**
     * Set the list of variables.
     * 
     * @param variableList the {@link List} with elements of type {@link Variable}
     */
    @Override
    public void setList(List<Variable> variableList) {
        if (variables != null) {
            clearList();
        }

        this.variables.addAll(variableList);
    }

    /**
     * Return the list of variables.
     * 
     * @return the field {@code variables}
     */
    @Override
    public List<Variable> getList() {
        return this.variables;
    }

    /**
     * Return a list of variables as things.
     * 
     * @return a {@link List} with elements of type {@link Variable} as {@link Thing}s
     */
    @Override
    public List<Thing> getAsThings() {
        List<Thing> things = new ArrayList<>();

        for(Variable variable: this.variables) {
            things.add(variable);
        }

        return things;
    }

    /**
     * Clear the list of varibles after notifying all observers of the upcoming modification.
     * 
     * @see #notifyObservers(AbstractModification)
     */
    @Override
    public void clearList() {
        for(Variable variable: this.variables) {
            variable.unregisterFromSubject();
            variable.notifyObservers(new VariableModification(ModificationType.DELETION, variable.getId()));
        }

        this.variables.clear();
    }
    
    /**
     * Build and return the display representation of every variable currently present in the field {@code variables}.
     * 
     * @return a {@link List} with elements of type {@link VariableDisplayRepresentation}
     */
    @Override
    public List<VariableDisplayRepresentation> getDisplayListRepresentation() {
        List<VariableDisplayRepresentation> displayRepresentation = new ArrayList<>();

        for(Variable variable: getList()) {
            displayRepresentation.add(new VariableDisplayRepresentation(variable));
        }

        return displayRepresentation;
    }

    /**
     * Search for a variable in {@code variables} based on its {@code id} or {@code name}.
     * 
     * @param identifier the identifier to match each {@link Variable}'s {@code id} and {@code name} against
     * @return the first matched {@link Variable} or throw a {@link VariableNotFoundException} if no {@link Variable}
     * with matching fields was found.
     */
    @Override
    public Variable getThing(String identifier) {
        for (Variable variable: getList()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equalsIgnoreCase(identifier)) {
                return variable;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    /**
     * Add a variable to the list of currently registered variables.
     * 
     * @param variable the {@link Variable} to add to {@code variables} 
     */
    @Override
    public void addThing(Variable variable) {
        this.variables.add(variable);
    }

    /**
     * Given a {@link String} identifier, delete a variable from the list of variables.
     * 
     * @param identifier either the name or the uuid of the {@link Variable} that should be removed from {@code variables}
     */
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

    /**
     * Check whether the provided {@code name} is valid.
     * A name is valid if
     *     - it is not a {@link UUID}
     *     - it is not used by any other {@link Variable} in the list of variables
     * 
     * @param name the {@code name} of the {@link Variable} under test
     * @param uuid the {@code id} of the {@link Variable} under test
     * @return {@code true} if the name is unique
     */
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

    /**
     * Check whether the a variable with the provided identifier is present in the list of variables.
     * 
     * A {@link VariableNotFoundException} is thrown if no {@link Variable} with matching {@code name} or {@code id} is found.
     * 
     * @param identifier the {@code name} or {@code id} of the {@link Variable} in question
     */
    public void checkIfIdIsPresent(String identifier) {
        for (Variable variable: getList()) {
            if (variable.getId().toString().equals(identifier) || variable.getName().equals(identifier.toLowerCase())) {
                return;
            }
        }

        throw new VariableNotFoundException(identifier);
    }

    /**
     * Return the list of self links.
     * These include all currently available HTTP endpoints for {@code /api/vars/}.
     * 
     * @param label the label which is used to construct each {@link LinkEntry}
     * @return the list with elements of type {@link LinkEntry} of currently available HTTP endpoints for {@code /api/vars/}
     */
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
    
    /**
     * Update the links of all currently registered variables and call {@link com.vs.foosh.api.model.variable.Variable#updateLinks() updateLinks()}
     * for every {@link Variable} in {@code variables}.
     */
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
