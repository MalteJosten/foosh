package com.vs.foosh.api.services;

import com.vs.foosh.api.model.device.AbstractDeviceList;

public class ListService {
    private static AbstractDeviceList abstractDevices;
    
    public static AbstractDeviceList getAbstractDeviceList() {
        if (abstractDevices == null) {
            abstractDevices = new AbstractDeviceList();
        }

        return abstractDevices;
    }

    public static void setAbstractDeviceList(AbstractDeviceList newDevices) {
        abstractDevices = newDevices;
    }

}
