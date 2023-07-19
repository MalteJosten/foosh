package com.vs.foosh.api.model;

import java.util.List;

public class AbstractDeviceDisplayRepresentation {
    private AbstractDevice device;
    private List<LinkEntry> links;

    public AbstractDeviceDisplayRepresentation(AbstractDevice device, List<LinkEntry> links) {
        this.device = device;
        this.links  = links;
    }

    public AbstractDevice getDevice() {
        return this.device;
    }

    public List<LinkEntry> getLinks() {
        return this.links;
    }
}
