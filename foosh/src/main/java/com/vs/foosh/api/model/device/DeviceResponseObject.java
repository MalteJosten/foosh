package com.vs.foosh.api.model.device;

import com.vs.foosh.api.model.misc.Thing;

/**
 * A data component that is a subclass of {@link Thing} and only includes fields of {@link AbstractDevice} which are relevant when using a {@link AbstractDevice} in a HTTP response.
 * 
 * The relevant fields include the device's {@code id}, {@code name}, {@code deviceName}, {@code type}, and {@code description}.
 */
public class DeviceResponseObject extends Thing {
    private String deviceName;
    private String type;
    private AbstractDeviceDescription description;

    /**
     * Given a device, create the corresponding {@code DeviceResponseObject}.
     *
     * @param device the {@link AbstractDevice} to create the {@code DeviceResponseObject} from 
     */
    public DeviceResponseObject(AbstractDevice device) {
        this.id          = device.getId();
        this.name        = device.getName();
        this.deviceName  = device.getDeviceName();
        this.type        = device.getType();
        this.description = device.getDescription();
    }

    /**
     * Return the {@code deviceName}.
     * 
     * @return the {@code deviceName}
     */
    public String getDeviceName() {
        return this.deviceName;
    }

    /**
     * Return the {@code type}.
     * 
     * @return the {@code type}
     */
    public String getType() {
        return this.type;
    }

    /**
     * Return the {@code description}.
     * 
     * @return the {@code description}
     */
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
