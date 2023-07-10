package com.vs.foosh.custom;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.vs.foosh.api.model.AbstractDevice;
import com.vs.foosh.api.model.DeviceList;
import com.vs.foosh.api.model.QueryNamePatchRequest;
import com.vs.foosh.api.services.LinkBuilder;

public class Device extends AbstractDevice {

    public Device(JsonNode description) {
        this.id          = UUID.randomUUID();
        this.description = new DeviceDescription(description);
        setObjectFields();

        setQueryName(DeviceList.findUniqueQueryName(new QueryNamePatchRequest(this.id.toString(), this.description.getProperties().get("name").toString())));

        this.links = new HashMap<>();
        this.links.put("selfStatic", LinkBuilder.buildPath(List.of("device", this.id.toString())).toString());
        this.links.put("selfQuery",  LinkBuilder.buildPath(List.of("device", this.queryName)).toString());
        this.links.put("devices", LinkBuilder.getDeviceListLink().toString());
    }        

    public Device(JsonNode description, String queryName) {
        this.id          = UUID.randomUUID();
        this.description = new DeviceDescription(description);
        setObjectFields();

        setQueryName(DeviceList.findUniqueQueryName(new QueryNamePatchRequest(this.id.toString(), queryName)));

        this.links = new HashMap<>();
        this.links.put("selfStatic", LinkBuilder.buildPath(List.of("device", this.id.toString())).toString());
        this.links.put("selfQuery",  LinkBuilder.buildPath(List.of("device", this.queryName)).toString());
        this.links.put("devices",    LinkBuilder.getDeviceListLink().toString());
    }

    @Override
    protected void setObjectFields() {
        this.deviceName = this.description.getProperties().get("name").toString();
        this.type       = this.description.getProperties().get("type").toString();
    }
}