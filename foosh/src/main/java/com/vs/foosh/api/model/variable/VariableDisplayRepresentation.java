package com.vs.foosh.api.model.variable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

/**
 * A data type containing a {@link VariableResponseObject} and a {@link List} of {@link LinkEntry}
 * to be used in HTTP responses.
 */
public class VariableDisplayRepresentation {
    /**
     * The {@link VariableResponseObject} holding a "reduced" version of a variable.
     */
    private VariableResponseObject variable;

    /**
     * The list of {@link LinkEntry} that holds the relevant link information for the current respond.
     */
    private List<LinkEntry> links;

    /**
     * Creates a {@code VariableDisplayRepresentation} given an {@link Variable}.
     * 
     * @param variable the {@link Variable} to construct the display representation from
     */
    public VariableDisplayRepresentation(Variable variable) {
        this.variable = new VariableResponseObject(variable);
        this.links    = variable.getSelfLinks();
    }

    /**
     * Return the variable's response object.
     * 
     * @return the field {@code variable}
     */
    public VariableResponseObject getVariable() {
        return this.variable;
    }

    /**
     * Return the list of links.
     * 
     * {@link JsonProperty} is used to name the field {@code links} "_links" in (de)serialization.
     * 
     * @return the field {@code links}
     */
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
