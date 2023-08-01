package com.vs.foosh.api.model.device;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

// TODO: @Override toString()
public class DeviceDisplayRepresentation {
    private DeviceResponseObject device;
    private List<LinkEntry> links;

    public DeviceDisplayRepresentation(AbstractDevice device) {
        this.device = new DeviceResponseObject(device);
        this.links  = device.getSelfLinks();
    }

    public DeviceResponseObject getDevice() {
        return this.device;
    }

    @JsonProperty("_links")
    public List<LinkEntry> getLinks() {
        return this.links;
    }
}
