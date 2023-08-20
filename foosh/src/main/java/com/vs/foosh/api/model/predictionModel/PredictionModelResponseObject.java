package com.vs.foosh.api.model.predictionModel;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;

/**
 * A data component that is a subclass of {@link Thing} and only includes fields of {@link AbstractPredictionModel} which are relevant when using a {@link AbstractDevice} in a HTTP response.
 * 
 * The relevant fields include the prediction model's {@code variableIds}, {@code parameters}, and {@code mappings}.
 */
public class PredictionModelResponseObject extends Thing {
    private List<UUID> variableIds;
    private List<String> parameters;
    private List<VariableParameterMapping> mappings;

    /**
     * Given a prediction model, create the corresponding {@code PredictionModelResponseObject}.
     * 
     * @param model the {@link AbstractPredictionModel to create the {@code PredictionModelResponseObject} from
     */
    public PredictionModelResponseObject(AbstractPredictionModel model) {
        this.id          = model.getId();
        this.name        = model.getName();
        this.variableIds = model.getVariableIds();
        this.parameters  = model.getParameters();
        this.mappings    = model.getAllMappings();
    }

    /**
     * Return the list of variable IDs.
     * 
     * @return the field {@code variableIds}
     */
    public List<UUID> getVariableIds() {
        return this.variableIds;
    }

    /**
     * Return the list of parameters.
     * 
     * @return the field {@code parameters}
     */
    public List<String> getParameters() {
        return this.parameters;
    }

    /**
     * Return the list of {@link VariableParameterMapping}s.
     * 
     * @return the field {@code mappings}
     */
    public List<VariableParameterMapping> getMappings() {
        return this.mappings;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< PredictionModelResponseObject >>\n");
        builder.append("ID:           " + id + "\n");
        builder.append("Name:         " + name + "\n");
        builder.append("Variable-IDs: " + variableIds + "\n");
        builder.append("Parameters:   " + parameters + "\n");
        builder.append("Mappings:     " + mappings);

        return builder.toString();
    }

}
