package com.vs.foosh.api.services;

import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.predictionModel.PredictionModelList;
import com.vs.foosh.api.model.variable.VariableList;

public class ListService {
    private static DeviceList abstractDevices;
    private static VariableList variables;
    private static PredictionModelList abstractPredictionModels;
    
    public static DeviceList getAbstractDeviceList() {
        if (abstractDevices == null) {
            abstractDevices = new DeviceList();
        }

        return abstractDevices;
    }

    public static void setAbstractDeviceList(DeviceList newDevices) {
        abstractDevices = newDevices;
    }

    public static VariableList getVariableList() {
        if (variables == null) {
            variables = new VariableList();
        }

        return variables;
    }

    public static void setVariableList(VariableList newVariables) {
        variables = newVariables;
    }

    public static PredictionModelList getAbstractPredictionModelList() {
        if (abstractPredictionModels == null) {
            abstractPredictionModels = new PredictionModelList();
        }

        return abstractPredictionModels;
    }

    public static void setAbstractPredictionModelList(PredictionModelList newPredictionModels) {
        abstractPredictionModels = newPredictionModels;
    }

}
