package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.SmartHomeInstruction;

public abstract class AbstractPredictionModel extends Thing {
    private Map<String, UUID> parameterMapping = new HashMap<>();

    /// This is a default implementation.
    /// Needs to be overwritten!
    public List<SmartHomeInstruction> makePrediction(String value) {
        return new ArrayList<>();
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
}
