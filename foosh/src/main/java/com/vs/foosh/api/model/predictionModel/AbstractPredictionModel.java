package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeInstruction;

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
}
