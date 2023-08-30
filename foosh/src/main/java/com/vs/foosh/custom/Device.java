package com.vs.foosh.custom;

import com.fasterxml.jackson.databind.JsonNode;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.DeviceNamePatchRequest;
import com.vs.foosh.api.services.helpers.ListService;

public class Device extends AbstractDevice {

    public Device(JsonNode description) {
        super("none");

        this.description = new DeviceDescription(description);
        setObjectFields();

        setName(ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(this.id, this.description.getProperties().get("name").toString())));
    }        

    public Device(JsonNode description, String name) {
        super("none");

        this.description = new DeviceDescription(description);
        setObjectFields();

        setName(ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(this.id, name)));
    }

    @Override
    protected void setObjectFields() {
        this.deviceName = this.description.getProperties().get("name").toString();
        this.type       = this.description.getProperties().get("type").toString();
    }

}