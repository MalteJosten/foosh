package com.vs.foosh.helper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.PredictionModelValueException;
import com.vs.foosh.api.exceptions.variable.CouldNotMakePredictionException;
import com.vs.foosh.api.exceptions.variable.VariablePredictionException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.helpers.ListService;

public class PredictionModelTest extends AbstractPredictionModel {

    private static final String FIXED_ID = "e0584b73-ad13-41f2-9f36-2297299d8671";

    public PredictionModelTest(String name) {
        this.id   = UUID.fromString(FIXED_ID);
        this.name = name;

        float[] myBounds = {0, 100};
        this.valueBounds = myBounds;

        setParameters(List.of("x1", "x2")); 
    }

    @Override
    public List<SmartHomeInstruction> makePrediction(UUID variableId, String value) {
        List<ParameterMapping> mappings = getMappings();

        if (mappings.isEmpty()) {
            throw new CouldNotMakePredictionException(
                variableId,
                "There are no mappings for the variable '" + ListService.getVariableList().getThing(variableId.toString()).getName() + "' (" + variableId + ") " +
                "and the model '" + this.name + "' (" + this.id + ")!"); 
        }

        List<SmartHomeInstruction> instructions = new ArrayList<>();
        AbstractDevice device = ListService.getDeviceList().getThing(mappings.get(0).getDeviceId());
        URI deviceSmartHomeURI;

        try {
            deviceSmartHomeURI = new URI(device.getDescription().getProperties().get("link").toString());

            SmartHomeInstruction instruction = new SmartHomeInstruction(
                1,
                UUID.fromString(mappings.get(0).getDeviceId()),
                "OFF",
                deviceSmartHomeURI
            );

            instructions.add(instruction);

        } catch (URISyntaxException e) {
            throw new VariablePredictionException(variableId, "An error occurred while creating smart home instructions.");
        }

        return instructions;

    }

    @Override
    public void checkValueInBounds(String variableId, Object value) {
        try {
            checkValueInThisBounds(variableId, (String) value);
        } catch (Exception e) {
            throw new PredictionModelValueException("Could not convert value. Please make sure to provide the value as a floating point number.");
        }
    }

    private void checkValueInThisBounds(String variableId, String value) {
        try {
            float floatValue = Float.parseFloat(value);
            float[] floatBounds = (float[]) this.valueBounds;
            if (floatValue > floatBounds[1] || floatValue < floatBounds[0]) {
                throw new PredictionModelValueException("The value exceeds the prediction value interval. Please provide a value between " + floatBounds[0] + " and " + floatBounds[1] + ".");
            }
        } catch (Exception e) {
            throw new PredictionModelValueException("Could not convert value to a floating point number. Please make sure to provide the value as a floating point number.");
        }

    }
    
    
}
