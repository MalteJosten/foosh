package com.vs.foosh.api.model;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.services.LinkBuilder;

public class Variable {
    private final UUID id;
    private String name;
    private List<UUID> models;
    private List<UUID> devices;

    private List<URI> modelLinks;
    private List<URI> deviceLinks;
    private List<LinkEntry> links;

    public Variable(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }


    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setModels(List<UUID> modelIDs) {
        this.models = modelIDs;
        updateModelLinks();
    }

    public void addModel(UUID modelID) {
        if (!this.models.contains(modelID)) {
            this.models.add(modelID);
            updateModelLinks();
        }
    }
    
    private void updateModelLinks() {
        modelLinks.clear();
        modelLinks.addAll(LinkBuilder.getModelBatchLink(this.models));
    }

    public List<URI> getModelLinks() {
        return this.modelLinks;
    }

    public void setDevices(List<UUID> deviceIDs) {
        this.devices = deviceIDs;
        updateDeviceLinks();
    }

    public void addDevice(UUID deviceID) {
        if (!this.devices.contains(deviceID)) {
            this.devices.add(deviceID);
            updateDeviceLinks();
        }
    }

    private void updateDeviceLinks() {
        deviceLinks.clear();
        deviceLinks.addAll(LinkBuilder.getDeviceBatchLink(this.devices));
    }

    public List<URI> getDeviceLinks() {
        return this.deviceLinks;
    }

    public List<LinkEntry> getLinks() {
        updateLinks();

        return links;
    }

    private void updateLinks() {
        // TODO: Update links
    }

}
