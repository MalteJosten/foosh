package com.vs.foosh.api.model.predictionModel;

import java.util.UUID;

import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.ModificationType;

public class PredictionModelModification extends AbstractModification {
    private UUID modelId;

    public PredictionModelModification(ModificationType modificationType, UUID modelId) {
        super(modificationType);
        this.modelId = modelId;
    }

    public UUID getModelId() {
        return this.modelId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< PredictionModelModification >>\n");
        builder.append("Model-ID:  "    + modelId + "\n");
        builder.append("Modification: " + modificationType);

        return builder.toString();
    }
    
}
