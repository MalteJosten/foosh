package com.vs.foosh.custom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.variable.VariablePredictionException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.ListService;

public class PredictionModelSR extends AbstractPredictionModel {

    public PredictionModelSR() {
        this.id   = UUID.randomUUID();
        this.name = "Symbolic Regression Model";

        setParameters(List.of("x1", "x2")); 
    }

    @Override
    public List<SmartHomeInstruction> makePrediction(UUID variableId, String value) {
        List<ParameterMapping> mappings = getMappings(variableId);

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
    
}
