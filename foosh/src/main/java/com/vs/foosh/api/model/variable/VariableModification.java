package com.vs.foosh.api.model.variable;

import java.util.UUID;

import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.ModificationType;

public class VariableModification extends AbstractModification {
    private UUID variableId;

    public VariableModification(ModificationType modificationType, UUID variableId) {
        super(modificationType);
        this.variableId = variableId;
    }

    public UUID getVariableId() {
        return this.variableId;
    }
    
}
