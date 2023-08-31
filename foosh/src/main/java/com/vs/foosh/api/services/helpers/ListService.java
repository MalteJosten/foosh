package com.vs.foosh.api.services.helpers;

import org.springframework.stereotype.Service;

import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.predictionModel.PredictionModelList;
import com.vs.foosh.api.model.variable.VariableList;

/**
 * A {@link Service} containing a Singleton of every instance of a {@link IThingList}.
 */
@Service
public class ListService {
    /**
     * The list of registered {@link AbstractDevice}s.
     */
    private static DeviceList devices;

    /**
     * The list of created {@link Variable}s.
     */
    private static VariableList variables;

    /**
     * The list of registered {@link AbstractPredictionModel}s.
     */
    private static PredictionModelList predictionModels;
    
    /**
     * Return the device list.
     * 
     * @return the field {@code devices}
     */
    public static DeviceList getDeviceList() {
        if (devices == null) {
            devices = new DeviceList();
        }

        return devices;
    }

    /**
     * Set the list of devices.
     * 
     * @param newDevices the {@link DeviceList} to set {@code devices} to
     */
    public static void setDeviceList(DeviceList newDevices) {
        devices = newDevices;
    }

    /**
     * Return the variable list.
     * 
     * @return the field {@code variables}
     */
    public static VariableList getVariableList() {
        if (variables == null) {
            variables = new VariableList();
        }

        return variables;
    }

    /**
     * Set the list of variables.
     * 
     * @param newVariables the {@link VariableList} to set {@code variables} to
     */
    public static void setVariableList(VariableList newVariables) {
        variables = newVariables;
    }

    /**
     * Return the prediction model list.
     * 
     * @return the field {@code predictionModels}
     */
    public static PredictionModelList getPredictionModelList() {
        if (predictionModels == null) {
            predictionModels = new PredictionModelList();
        }

        return predictionModels;
    }

    /**
     * Set the list of prediction models.
     * 
     * @param newPredicitonModels the {@link PredictionModelList} to set {@code predictionModels} to
     */
    public static void setPredictionModelList(PredictionModelList newPredictionModels) {
        predictionModels = newPredictionModels;
    }

}
