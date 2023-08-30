package com.vs.foosh.api.services.helpers;

import org.springframework.stereotype.Service;

import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.predictionModel.PredictionModelList;
import com.vs.foosh.api.model.variable.VariableList;

@Service
public class ListService {
    private static DeviceList devices;
    private static VariableList variables;
    private static PredictionModelList predictionModels;
    
    public static DeviceList getDeviceList() {
        if (devices == null) {
            devices = new DeviceList();
        }

        return devices;
    }

    public static void setDeviceList(DeviceList newDevices) {
        devices = newDevices;
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

    public static PredictionModelList getPredictionModelList() {
        if (predictionModels == null) {
            predictionModels = new PredictionModelList();
        }

        return predictionModels;
    }

    public static void setPredictionModelList(PredictionModelList newPredictionModels) {
        predictionModels = newPredictionModels;
    }

}
