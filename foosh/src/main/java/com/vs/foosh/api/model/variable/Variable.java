package com.vs.foosh.api.model.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.IThingListObserver;
import com.vs.foosh.api.model.misc.IThingListSubject;
import com.vs.foosh.api.model.misc.ModificationType;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

public class Variable extends Thing implements IThingListObserver, IThingListSubject {
    private List<UUID> models  = new ArrayList<>();
    private List<UUID> devices = new ArrayList<>();

    private List<LinkEntry> modelLinks  = new ArrayList<>();
    private List<LinkEntry> deviceLinks = new ArrayList<>();
    private List<LinkEntry> links       = new ArrayList<>();
    private List<LinkEntry> extLinks    = new ArrayList<>();

    private List<IThingListObserver> observers = new ArrayList<>();

    public Variable(String name, List<UUID> modelIds, List<UUID> deviceIds) {
        this.id      = UUID.randomUUID();
        this.name    = name;
        this.models  = modelIds;
        this.devices = deviceIds;
    }

    public void setName(String name) {
        this.name = name;
        updateLinks();
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
    
    public List<UUID> getModelIds() {
        return this.models;
    }

    public List<LinkEntry> getModelLinks() {
        return this.modelLinks;
    }

    public List<LinkEntry> getModels() {
        return this.modelLinks;
    }

    public void clearDevices() {
       unregisterFromSubject();
       this.devices.clear();
       updateDeviceLinks(); 
    }

    public void setDevices(List<UUID> deviceIDs) {
        this.devices = deviceIDs;
        updateDeviceLinks();
        registerToSubject();
    }

    public void addDevice(UUID deviceID) {
        if (!this.devices.contains(deviceID)) {
            this.devices.add(deviceID);
            updateDeviceLinks();
            registerToSubject();
        }
    }


    public List<UUID> getDeviceIds() {
        return this.devices;
    }

    public List<LinkEntry> getDeviceLinks() {
        return this.deviceLinks;
    }

    public List<LinkEntry> getDevices() {
        return this.deviceLinks;
    }

    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    public List<LinkEntry> getSelfStaticLinks(String label) {
        List<LinkEntry> links = new ArrayList<>();
       
        for(LinkEntry link: getSelfLinks()) {
            if (link.getRelation().equals("selfStatic")) {
                links.add(new LinkEntry(label, link.getLink(), link.getAction(), link.getTypes()));
            }
        }

        return links;
    }

    public List<LinkEntry> getExtLinks() {
        return this.extLinks;
    }

    public List<LinkEntry> getAllLinks() {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(getSelfLinks());
        links.addAll(getModelLinks());
        links.addAll(getDeviceLinks());
        links.addAll(getExtLinks());
        return links;
    }

    public VariableDisplayRepresentation getDisplayRepresentation() {
        return new VariableDisplayRepresentation(this);
    }

    public void updateLinks() {
        updateSelfLinks();
        updateDeviceLinks();
        updateModelLinks();
        updateExtLinks();
    }
    
    private void updateSelfLinks() {
        LinkEntry getId    = new LinkEntry("selfStatic", LinkBuilder.getVariableLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry patchId  = new LinkEntry("selfStatic", LinkBuilder.getVariableLink(this.id.toString()), HttpAction.PATCH, List.of("application/json"));
        LinkEntry deleteId = new LinkEntry("selfStatic", LinkBuilder.getVariableLink(this.id.toString()), HttpAction.DELETE, List.of());

        LinkEntry getName    = new LinkEntry("selfName", LinkBuilder.getVariableLink(this.name), HttpAction.GET, List.of());
        LinkEntry deleteName = new LinkEntry("selfName", LinkBuilder.getVariableLink(this.name), HttpAction.DELETE, List.of());

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, patchId, deleteId, getName, deleteName));
    }

    // TODO: Needs to be called after PATCH
    private void updateDeviceLinks() {
        if (deviceLinks != null || !deviceLinks.isEmpty()) {
            deviceLinks.clear();
        }

        for (UUID deviceId: devices) {
            deviceLinks.addAll(ListService.getDeviceList().getThing(deviceId.toString()).getSelfStaticLinks("device"));
        }
    }

    // TODO: Needs to be called after PATCH
    private void updateModelLinks() {

    }

    private void updateExtLinks() {
        if (extLinks != null || !extLinks.isEmpty()) {
            extLinks.clear();
        }

        extLinks.addAll(ListService.getVariableList().getLinks("variables"));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID:\t "     + id      + "\n");
        builder.append("Name:\t "   + name    + "\n");
        builder.append("Devices: "  + devices + "\n");
        builder.append("Models:\t " + models  + "\n");

        return builder.toString();
    }

    public void registerToSubject() {
        ListService.getDeviceList().attach(this);
    }

    // TODO: Needs to be called after DELETE vars/{id} and DELETE vars/{id}/devices
    public void unregisterFromSubject() {
        ListService.getDeviceList().detach(this);
    }

    @Override
    public void update(AbstractModification modification) {
        if (modification.getModificationType() == ModificationType.DELETION) {
            devices.clear();
        }

        updateLinks();

        notifyObservers(new VariableModification(ModificationType.DEPENDENCIES_CHANGED, this.id));
    }

    @Override
    public void attach(IThingListObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(IThingListObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(AbstractModification modification) {
        for(IThingListObserver observer: observers) {
            observer.update(modification);
        }

        if (modification.getModificationType() == ModificationType.DELETION) {
            observers.clear();
        }

    }

}
