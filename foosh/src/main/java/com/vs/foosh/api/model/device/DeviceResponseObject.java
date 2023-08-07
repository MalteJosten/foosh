package com.vs.foosh.api.model.device;

import com.vs.foosh.api.model.misc.Thing;

public class DeviceResponseObject extends Thing {
    private String deviceName;
    private String type;
    private AbstractDeviceDescription description;

    public DeviceResponseObject(AbstractDevice device) {
        this.id          = device.getId();
        this.name        = device.getName();
        this.deviceName  = device.getDeviceName();
        this.type        = device.getType();
        this.description = device.getDescription();
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getType() {
        return this.type;
    }

    public AbstractDeviceDescription getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceResponseObject >>\n");
        builder.append("ID:          " + id + "\n");
        builder.append("Name:        " + name + "\n");
        builder.append("deviceName:  " + deviceName + "\n");
        builder.append("Type:        " + type + "\n");
        builder.append("Description: " + description);

        return builder.toString();
    }
}
