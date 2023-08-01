package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.deser.AbstractDeserializer;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.LinkBuilder;

public abstract class AbstractPredictionModel extends Thing {
    private Map<String, UUID> parameterMapping = new HashMap<>();

    protected List<LinkEntry> links = new ArrayList<>();

    /// This is a default implementation.
    /// Needs to be overwritten!
    public List<SmartHomeInstruction> makePrediction(String value) {
        return new ArrayList<>();
    }

    public Map<String, UUID> getMapping() {
        return this.parameterMapping;
    }

    public void setMapping(Map<String, UUID> mapping) {
        this.parameterMapping = mapping;
    }

    public void patchMapping(FooSHJsonPatch patch) {
        // TODO: Implement
    }

    public void deleteMapping() {
        // TODO: Implement
    }

    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    public List<LinkEntry> getAllLinks() {
        List<LinkEntry> allLinks = new ArrayList<>();
        allLinks.addAll(links);

        return allLinks;
    }

    protected void updateSelfLinks() {
        LinkEntry getId   = new LinkEntry("selfStatic", LinkBuilder.getPredictionModelLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry getName = new LinkEntry("selfName", LinkBuilder.getPredictionModelLink(this.name), HttpAction.GET, List.of());

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, getName));
    }

    public PredictionModelDisplayRepresentation getDisplayRepresentation() {
        return new PredictionModelDisplayRepresentation(this);
    }
}
