package com.vs.foosh.api.model.predictionModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

/**
 * A data type containing a {@link PredictionModelResponseObject} and a {@link List} of {@link LinkEntry}
 * to be used in HTTP responses.
 */
public class PredictionModelDisplayRepresentation {
    /**
     * The {@link PredictionModelResponseObject} holding a "reduced" version of a prediction model.
     */
    private PredictionModelResponseObject model;

    /**
     * The list of {@link LinkEntry} that holds the relevant link information for the current respond.
     */
    private List<LinkEntry> links;

    /**
     * Creates a {@code PredictionModelDisplayRepresentation} given an {@link AbstractPredictionModel}.
     * 
     * @param model the {@link AbstractPredictionModel} to construct the display representation from
     */
    public PredictionModelDisplayRepresentation(AbstractPredictionModel model) {
        this.model = new PredictionModelResponseObject(model);
        this.links = model.getSelfLinks();
    }

    /**
     * Return prediction model's response object.
     * 
     * @return the field {@code model}
     */
    public PredictionModelResponseObject getModel() {
        return this.model;
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
        StringBuilder builder = new StringBuilder("<< PredictionModelDisplayRepresentation >>\n");
        builder.append("Model:\t" + model + "\n");
        builder.append("Links:\t" + links);
        
        return builder.toString();
    }
}
