package com.vs.foosh.api.model.predictionModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

public class PredictionModelDisplayRepresentation {
    private PredictionModelResponseObject model;
    private List<LinkEntry> links;

    public PredictionModelDisplayRepresentation(AbstractPredictionModel model) {
        this.model = new PredictionModelResponseObject(model);
        this.links = model.getSelfLinks();
    }

    public PredictionModelResponseObject getModel() {
        return this.model;
    }

    @JsonProperty("_links")
    public List<LinkEntry> getLinks() {
        return this.links;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< AbstractPredictionModelDisplayRepresentation >>\n");
        builder.append("model:\t" + model + "\n");
        builder.append("links:\t" + links);
        
        return builder.toString();
    }
}
