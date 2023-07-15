package com.vs.foosh.api.model;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractDevice {
    protected UUID id;
    protected String queryName;
    protected String deviceName;
    protected String type;
    protected AbstractDeviceDescription description;

    protected Map<String, String> links;

    protected abstract void setObjectFields();

    public UUID getId() {
        return this.id;
    }

    public String getQueryName() {
        return this.queryName;
    }

    public void setQueryName(String name) {
        this.queryName = name.toLowerCase();
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

    public void setDescription(AbstractDeviceDescription description) {
        this.description = description;
    }

    public Map<String, String> getLinks() {
        return this.links;
    }

    @JsonIgnore
    public String getStaticLink() {
        return this.links.get("selfStatic");
    }

    @JsonIgnore
    public String getQueryLink() {
        return this.links.get("selfQuery");
    }

    @Override
    public String toString() {
        return "Device: " + id + "\nName: " + deviceName + "\nQuery-Name: " + queryName + "\nType: " + type
                + "\nlinks: " + links.toString();
    }
}