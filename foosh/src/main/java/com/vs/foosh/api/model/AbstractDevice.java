package com.vs.foosh.api.model;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.vs.foosh.api.services.LinkBuilder;

public abstract class AbstractDevice implements Serializable {
    protected UUID id;
    protected String queryName;
    protected String deviceName;
    protected String type;
    protected AbstractDeviceDescription description;

    protected Map<String, URI> links;

    protected abstract void setObjectFields();

    public UUID getId() {
        return this.id;
    }

    public String getQueryName() {
        return this.queryName;
    }

    public void setQueryName(String name) {
        this.queryName = name.toLowerCase();
        if (this.links != null) {
            this.links.replace("selfQuery", LinkBuilder.buildPath(List.of("device", this.queryName)));
        }
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

    @JsonIgnore
    public Map<String, URI> getLinks() {
        return this.links;
    }

    public void setLinks() {
        this.links = new HashMap<>();
        this.links.put("selfStatic", LinkBuilder.buildPath(List.of("device", this.id.toString())));
        this.links.put("selfQuery",  LinkBuilder.buildPath(List.of("device", this.queryName)));
        this.links.put("devices",    LinkBuilder.getDeviceListLink());
    }

    @JsonIgnore
    public URI getStaticLink() {
        return this.links.get("selfStatic");
    }

    @JsonIgnore
    public URI getQueryLink() {
        return this.links.get("selfQuery");
    }

    @Override
    public String toString() {
        return "Device: " + id + "\nName: " + deviceName + "\nQuery-Name: " + queryName + "\nType: " + type
                + "\nlinks: " + links.toString();
    }
}