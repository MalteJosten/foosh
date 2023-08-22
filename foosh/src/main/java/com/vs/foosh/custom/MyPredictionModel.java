package com.vs.foosh.custom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.PredictionModelValueException;
import com.vs.foosh.api.exceptions.variable.CouldNotMakePredictionException;
import com.vs.foosh.api.exceptions.variable.VariablePredictionException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.ListService;

public class MyPredictionModel extends AbstractPredictionModel {

    private static final String FIXED_ID = "e0584b73-ad13-41f2-9f36-2297299d8670";

    public MyPredictionModel() {
        this.id   = UUID.fromString(FIXED_ID);
        this.name = "my-model";

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

        String state = determineLightState(Float.parseFloat(value));

        List<SmartHomeInstruction> instructions = new ArrayList<>();
        AbstractDevice device = ListService.getDeviceList().getThing(mappings.get(0).getDeviceId());
        URI deviceSmartHomeURI;

        try {
            deviceSmartHomeURI = new URI(device.getDescription().getProperties().get("link").toString());

            SmartHomeInstruction instruction = new SmartHomeInstruction(
                1,
                UUID.fromString(mappings.get(0).getDeviceId()),
                state,
                deviceSmartHomeURI
            );

            instructions.add(instruction);

        } catch (URISyntaxException e) {
            throw new VariablePredictionException(variableId, "An error occurred while creating smart home instructions.");
        }

        return instructions;

    }

    private String determineLightState(float value) {
        Calendar now = Calendar.getInstance();
        int nowHours   = now.get(Calendar.HOUR_OF_DAY);
        int nowMinutes = now.get(Calendar.MINUTE);

        // We need to substract 2 hours in order to convert hours from UTC+2 to UTC+0
        nowHours -= 2;
        if (nowHours < 0) {
            nowHours = 24 - nowHours;
        }

        int minutesOfDay = nowHours * 60 + nowMinutes;

        float offValue = lampOffValue((float) minutesOfDay);

        // To potentially save energy, we first check whether we can achieve the desired value by not turning on the light.
        if (value <= offValue) {
            return "OFF";
        } 

        // Turn the light on, if we can't reach the desired level of brightness without the light.
        // We also turn on the light, if we just cannot reach the value at all (e.g., 100%).
        // This model does its best, to reach the desired level and tries to come as close as possible.
        return "ON";

    }

    private float lampOffValue(float time) {
        if (time <= 210) {
            return 1.25f;
        } else if (time <= 420) {
            return 0.3602f   * time - 74.34f;
        } else if (time <= 980) {
            return 0.0295f   * time + 64.57f;
        } else if (time <= 1170) {
            return -0.04815f * time + 565.3242f;
        } else {
            return -0.0029f  * time + 5.38f;
        }
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
