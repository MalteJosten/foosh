package com.vs.foosh.api.model.variable;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;

/**
 * A data component that is a subclass of {@link Thing} and only includes some fields of {@link Variable} which are relevant when using a {@link Variable} in a HTTP response.
 * 
 * The relevant fields include the variable's {@code variableIds} and {@code deviceIds}.
 */
public class VariableResponseObject extends Thing {
    private List<UUID> modelIds;
    private List<UUID> deviceIds;

    /**
     * Given a variable, create the corresponding {@code VariableResponseObject}.
     * 
     * @param variable the {@link Variable} to create the {@code VariableResponseObject} from
     */
    public VariableResponseObject(Variable variable) {
        this.id        = variable.getId();
        this.name      = variable.getName();
        this.modelIds  = variable.getModelIds();
        this.deviceIds = variable.getDeviceIds();
    }

    /**
     * Return the list of {@link AbstractPredictionModel}s' IDs.
     * 
     * @return the field {@code modelIds}
     */
    public List<UUID> getModelIds() {
        return this.modelIds;
    }

    /**
     * Return the list of {@link AbstractDevice}s' IDs.
     * 
     * @return the field {@code deviceIds}
     */
    public List<UUID> getDeviceIds() {
        return this.deviceIds;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< VariableResponseObject >>\n");
        builder.append("ID:   " + id + "\n");
        builder.append("Name: " + name + "\n");
        builder.append("Model-IDs:  " + modelIds + "\n");
        builder.append("Device-IDs: " + deviceIds);
        
        return builder.toString();
    }

}
