package com.vs.foosh.api.model.predictionModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

public class AbstractPredictionModelDisplayRepresentation {
    private AbstractPredictionModelResponseObject model;
    private List<LinkEntry> links;

    public AbstractPredictionModelDisplayRepresentation(AbstractPredictionModel model) {
        this.model = new AbstractPredictionModelResponseObject();
        this.links = model.getSelfLinks();
    }

    public AbstractPredictionModelResponseObject getResponseObject() {
        return this.model;
    }

    @JsonProperty("_links")
    public List<LinkEntry> getLinks() {
        return this.links;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< AbstractPredictionModelDisplayRepresentation >>");
        builder.append("model:\t" + model + "\n");
        builder.append("links:\t" + links);
        
        return builder.toString();
    }
}
