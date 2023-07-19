package com.vs.foosh.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vs.foosh.api.services.LinkBuilder;

public class Variable extends HttpResponseObject {
    private final UUID id;
    private String name;
    private List<UUID> models  = new ArrayList<>();
    private List<UUID> devices = new ArrayList<>();

    private List<LinkEntry> modelLinks  = new ArrayList<>();
    private List<LinkEntry> deviceLinks = new ArrayList<>();
    private List<LinkEntry> links       = new ArrayList<>();
    private List<LinkEntry> extLinks    = new ArrayList<>();

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
    
    @JsonIgnore
    public List<UUID> getModels() {
        return this.models;
    }

    @JsonIgnore
    public List<LinkEntry> getModelLinks() {
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


    @JsonIgnore
    public List<UUID> getDevices() {
        return this.devices;
    }

    @JsonIgnore
    public List<LinkEntry> getDeviceLinks() {
        return this.deviceLinks;
    }

    @JsonIgnore
    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    @JsonIgnore
    public List<LinkEntry> getExtLinks() {
        return this.extLinks;
    }

    public void updateLinks() {
        updateSelfLinks();
        updateDeviceLinks();
        updateModelLinks();
        updateExtLinks();
    }
    
    private void updateSelfLinks() {
        LinkEntry getId    = new LinkEntry("selfStatic", LinkBuilder.getVariableLink(this.id), HttpAction.GET, List.of());
        LinkEntry patchId  = new LinkEntry("selfStatic", LinkBuilder.getVariableLink(this.id), HttpAction.PATCH, List.of("application/json"));
        LinkEntry deleteId = new LinkEntry("selfStatic", LinkBuilder.getVariableLink(this.id), HttpAction.DELETE, List.of());

        LinkEntry getName    = new LinkEntry("selfName", LinkBuilder.buildPath(List.of("variable", this.name)), HttpAction.GET, List.of());
        LinkEntry deleteName = new LinkEntry("selfName", LinkBuilder.buildPath(List.of("variable", this.name)), HttpAction.DELETE, List.of());

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, patchId, deleteId, getName, deleteName));
    }

    private void updateDeviceLinks() {
        if (deviceLinks != null || !deviceLinks.isEmpty()) {
            deviceLinks.clear();
        }

        for (UUID deviceId: devices) {
            System.out.println(deviceId);
            deviceLinks.addAll(DeviceList.getDevice(deviceId.toString()).getSelfLinks());
        }
    }

    private void updateModelLinks() {
        // TODO: Needs to be called after PUT or PATCH
    }

    private void updateExtLinks() {
        if (extLinks != null || !extLinks.isEmpty()) {
            extLinks.clear();
        }

        extLinks.addAll(VariableList.getLinks("variables"));
    }

}
