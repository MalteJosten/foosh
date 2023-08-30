package com.vs.foosh.api.model.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.IThingListSubscriber;
import com.vs.foosh.api.model.misc.IThingListPublisher;
import com.vs.foosh.api.model.misc.ModificationType;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.helpers.LinkBuilderService;
import com.vs.foosh.api.services.helpers.ListService;

public class Variable extends Thing implements IThingListSubscriber, IThingListPublisher {
    protected List<UUID> models  = new ArrayList<>();
    protected List<UUID> devices = new ArrayList<>();

    protected List<LinkEntry> links          = new ArrayList<>();
    protected List<LinkEntry> extLinks       = new ArrayList<>();
    protected List<LinkEntry> modelLinks     = new ArrayList<>();
    protected List<LinkEntry> deviceLinks    = new ArrayList<>();
    protected List<LinkEntry> varDeviceLinks = new ArrayList<>();
    protected List<LinkEntry> varModelLinks  = new ArrayList<>();

    protected List<IThingListSubscriber> observers = new ArrayList<>();

    public Variable(String name, List<UUID> modelIds, List<UUID> deviceIds) {
        this.id   = UUID.randomUUID();
        this.name = name;
        this.models.clear();
        this.models.addAll(modelIds);
        this.devices.clear();
        this.devices.addAll(deviceIds);
    }

    public void setName(String name) {
        this.name = name;
        updateLinks();
    }

    public void setModels(List<UUID> modelIDs) {
        this.models.clear();
        this.models.addAll(modelIDs);
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

    public void clearDevices() {
       unregisterFromSubject();
       this.devices.clear();
       updateDeviceLinks(); 
    }

    public void clearModels() {
        notifyObservers(new VariableModification(ModificationType.DELETION, this.id));
        this.models.clear();
        updateVarModelLinks();
    }

    public void setDevices(List<UUID> deviceIDs) {
        this.devices.clear();
        this.devices.addAll(deviceIDs);
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

    public List<LinkEntry> getVarDeviceLinks() {
        return this.varDeviceLinks;
    }

    public List<LinkEntry> getVarModelLinks() {
        return this.varModelLinks;
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
        updateVarDeviceLinks();
    }
    
    protected void updateSelfLinks() {
        LinkEntry getId    = new LinkEntry("selfStatic", LinkBuilderService.getVariableLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry patchId  = new LinkEntry("selfStatic", LinkBuilderService.getVariableLink(this.id.toString()), HttpAction.PATCH, List.of("application/json"));
        LinkEntry deleteId = new LinkEntry("selfStatic", LinkBuilderService.getVariableLink(this.id.toString()), HttpAction.DELETE, List.of());

        LinkEntry getName    = new LinkEntry("selfName", LinkBuilderService.getVariableLink(this.name), HttpAction.GET, List.of());
        LinkEntry deleteName = new LinkEntry("selfName", LinkBuilderService.getVariableLink(this.name), HttpAction.DELETE, List.of());

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, patchId, deleteId, getName, deleteName));
    }

    protected void updateDeviceLinks() {
        if (deviceLinks != null || !deviceLinks.isEmpty()) {
            deviceLinks.clear();
        }

        for (UUID deviceId: devices) {
            deviceLinks.addAll(ListService.getDeviceList().getThing(deviceId.toString()).getSelfStaticLinks("device"));
        }
    }

    protected void updateModelLinks() {
        if (modelLinks != null || !modelLinks.isEmpty()) {
            modelLinks.clear();
        }

        for (UUID modelId: models) {
            modelLinks.addAll(ListService.getPredictionModelList().getThing(modelId.toString()).getSelfStaticLinks("model"));
        }
    }

    protected void updateExtLinks() {
        if (extLinks != null || !extLinks.isEmpty()) {
            extLinks.clear();
        }

        extLinks.addAll(ListService.getVariableList().getLinks("variables"));
    }

    protected void updateVarDeviceLinks() {
        LinkEntry get    = new LinkEntry("devices", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry("devices", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.POST, List.of("application/json"));
        LinkEntry patch  = new LinkEntry("devices", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.PATCH, List.of("application/json-patch+json"));
        LinkEntry delete = new LinkEntry("devices", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.DELETE, List.of());


        if (varDeviceLinks != null || !varDeviceLinks.isEmpty()) {
            varDeviceLinks.clear();
        }

        if (varDeviceLinks.isEmpty()) {
            varDeviceLinks.addAll(List.of(get, post, patch));
        } else {
            varDeviceLinks.addAll(List.of(get, patch, delete));
        }
    }

    protected void updateVarModelLinks() {
        LinkEntry get    = new LinkEntry("models", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry("models", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.POST, List.of("application/json"));
        LinkEntry patch  = new LinkEntry("models", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.PATCH, List.of("application/json-patch+json"));
        LinkEntry delete = new LinkEntry("models", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.DELETE, List.of());


        if (varModelLinks != null || !varModelLinks.isEmpty()) {
            varModelLinks.clear();
        }

        varModelLinks.addAll(List.of(get, post, patch));
        if (!varModelLinks.isEmpty()) {
            varModelLinks.add(delete);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< Variable >>\n");
        builder.append("ID:\t "     + id      + "\n");
        builder.append("Name:\t "   + name    + "\n");
        builder.append("Devices: "  + devices + "\n");
        builder.append("Models:\t " + models  + "\n");

        return builder.toString();
    }

    public void registerToSubject() {
        ListService.getDeviceList().attach(this);
    }

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
    public void attach(IThingListSubscriber observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            AbstractPredictionModel model = (AbstractPredictionModel) observer;
            models.add(model.getId());
        }

        updateModelLinks();
    }

    @Override
    public void detach(IThingListSubscriber observer) {
        observers.remove(observer);
        AbstractPredictionModel model = (AbstractPredictionModel) observer;
        models.remove(model.getId());

        updateModelLinks();
    }

    @Override
    public void notifyObservers(AbstractModification modification) {
        for(IThingListSubscriber observer: observers) {
            observer.update(modification);
        }

        if (modification.getModificationType() == ModificationType.DELETION) {
            observers.clear();
        }

    }

}
