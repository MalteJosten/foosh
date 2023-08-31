package com.vs.foosh.api.model.variable;

import java.util.UUID;

import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.ModificationType;

/**
 * A subclass of {@link AbtractModification} with information about the modification of a {@link Variable}.  
 */
public class VariableModification extends AbstractModification {
    /**
     * The {@link UUID} of the {@link Variable} which this modification applies to.
     */
    private UUID variableId;

    /**
     * Create a {@code VariableModification}.
     * 
     * @param modificationType the type of the modification as a {@link ModificationType}
     * @param variableId the {@link UUID} of the {@link Variable} in question
     */
    public VariableModification(ModificationType modificationType, UUID variableId) {
        super(modificationType);
        this.variableId = variableId;
    }

    /**
     * Return the variable's ID.
     * 
     * @return the field {@code variableId}
     */
    public UUID getVariableId() {
        return this.variableId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableModification >>\n");
        builder.append("Variable-ID:  " + variableId + "\n");
        builder.append("Modification: " + modificationType);

        return builder.toString();
    }
    
}
