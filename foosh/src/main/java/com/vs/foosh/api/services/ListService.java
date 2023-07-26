package com.vs.foosh.api.services;

import com.vs.foosh.api.model.device.AbstractDeviceList;
import com.vs.foosh.api.model.variable.VariableList;

public class ListService {
    private static AbstractDeviceList abstractDevices;
    private static VariableList variables;
    
    public static AbstractDeviceList getAbstractDeviceList() {
        if (abstractDevices == null) {
            abstractDevices = new AbstractDeviceList();
        }

        return abstractDevices;
    }

    public static void setAbstractDeviceList(AbstractDeviceList newDevices) {
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
}
