package com.vs.foosh.custom;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.device.QueryNamePatchRequest;

public class Device extends AbstractDevice {

    public Device(JsonNode description) {
        this.id          = UUID.randomUUID();
        this.description = new DeviceDescription(description);
        setObjectFields();

        setQueryName(DeviceList.findUniqueQueryName(new QueryNamePatchRequest(this.id, this.description.getProperties().get("name").toString())));
    }        

    public Device(JsonNode description, String queryName) {
        this.id          = UUID.randomUUID();
        this.description = new DeviceDescription(description);
        setObjectFields();

        setQueryName(DeviceList.findUniqueQueryName(new QueryNamePatchRequest(this.id, queryName)));
    }

    @Override
    protected void setObjectFields() {
        this.deviceName = this.description.getProperties().get("name").toString();
        this.type       = this.description.getProperties().get("type").toString();
    }
}