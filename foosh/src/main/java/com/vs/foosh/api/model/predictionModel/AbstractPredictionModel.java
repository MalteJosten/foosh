package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.List;

import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

public abstract class AbstractPredictionModel extends Thing {
    private List<ParameterMapping> parameterMapping = new ArrayList<>();

    protected List<LinkEntry> links = new ArrayList<>();
    protected List<LinkEntry> deviceLinks = new ArrayList<>();

    /// This is a default implementation.
    /// Needs to be overwritten!
    public List<SmartHomeInstruction> makePrediction(String value) {
        return new ArrayList<>();
    }

    public List<ParameterMapping> getMapping() {
        return this.parameterMapping;
    }

    public void setMapping(List<ParameterMapping> mapping) {
        this.parameterMapping.clear();
        this.parameterMapping.addAll(mapping);
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

    public List<LinkEntry> getDeviceLinks() {
        return this.deviceLinks;
    }

    public List<LinkEntry> getAllLinks() {
        List<LinkEntry> allLinks = new ArrayList<>();
        allLinks.addAll(this.links);
        allLinks.addAll(this.deviceLinks);

        return allLinks;
    }

    public void updateLinks() {
        updateSelfLinks();
        updateDeviceLinks();
    }

    protected void updateSelfLinks() {
        LinkEntry getId   = new LinkEntry("selfStatic", LinkBuilder.getPredictionModelLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry getName = new LinkEntry("selfName", LinkBuilder.getPredictionModelLink(this.name), HttpAction.GET, List.of());

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, getName));
    }

    protected void updateDeviceLinks() {
        if (deviceLinks != null || !deviceLinks.isEmpty()) {
            deviceLinks.clear();
        }

        for (ParameterMapping mapping: parameterMapping) {
            deviceLinks.addAll(ListService.getDeviceList().getThing(mapping.getDeviceId().toString()).getSelfStaticLinks("device"));
        }
    }

    public PredictionModelDisplayRepresentation getDisplayRepresentation() {
        return new PredictionModelDisplayRepresentation(this);
    }
}
