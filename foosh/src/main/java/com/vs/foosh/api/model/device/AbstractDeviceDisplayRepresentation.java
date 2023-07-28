package com.vs.foosh.api.model.device;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

// TODO: @Override toString()
public class AbstractDeviceDisplayRepresentation {
    private AbstractDeviceResponseObject device;
    private List<LinkEntry> links;

    public AbstractDeviceDisplayRepresentation(AbstractDevice device) {
        this.device = new AbstractDeviceResponseObject(device);
        this.links  = device.getSelfLinks();
    }

    public AbstractDeviceResponseObject getDevice() {
        return this.device;
    }

    @JsonProperty("_links")
    public List<LinkEntry> getLinks() {
        return this.links;
    }
}
