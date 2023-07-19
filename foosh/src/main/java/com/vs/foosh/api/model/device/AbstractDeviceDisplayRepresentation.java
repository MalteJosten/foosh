package com.vs.foosh.api.model.device;

import java.util.List;

import com.vs.foosh.api.model.web.LinkEntry;

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
