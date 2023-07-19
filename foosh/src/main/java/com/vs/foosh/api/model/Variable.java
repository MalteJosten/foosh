package com.vs.foosh.api.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vs.foosh.api.services.LinkBuilder;

public class Variable {
    private final UUID id;
    private String name;
    private List<UUID> models  = new ArrayList<>();
    private List<UUID> devices = new ArrayList<>();

    private List<URI> modelLinks  = new ArrayList<>();
    private List<URI> deviceLinks = new ArrayList<>();
    private List<LinkEntry> links = new ArrayList<>();

    public Variable(String name, List<UUID> modelIds, List<UUID> deviceIds) {
        this.id      = UUID.randomUUID();
        this.name    = name;
        this.models  = modelIds;
        this.devices = deviceIds;
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

    @JsonIgnore
    public List<UUID> getModels() {
        return this.models;
    }

    @JsonIgnore
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

    @JsonIgnore
    public List<UUID> getDevices() {
        return this.devices;
    }

    @JsonIgnore
    public List<URI> getDeviceLinks() {
        return this.deviceLinks;
    }

    public List<LinkEntry> getLinks() {
        updateLinks();

        return links;
    }

    private void updateLinks() {
        // TODO: Update links
        if (modelLinks != null || !modelLinks.isEmpty()) {
            modelLinks.clear();
        }

        for (UUID modelId: models) {

        }
    }

}
