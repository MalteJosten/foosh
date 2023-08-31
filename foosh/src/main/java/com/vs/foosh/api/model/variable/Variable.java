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

/**
 * A subclass of {@link Thing} that adds support for (environment) variables.
 * It adds the additional fields:
 *      * modelIds
 *      * deviceIds
 *      * links
 *      * extLinks
 *      * modelLinks
 *      * deviceLinks
 *      * varDeviceLinks
 *      * varModelLinks
 * These add variable specific data and allows the linking to/with deviceIds and prediction modelIds.
 * 
 * It implements the interface {@link IThingListSubscriber} to act as a subscriber of the observer pattern.
 * It implements the interface {@link IThingListPublisher} to act as a publisher of the observer pattern.
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 * 
 */
public class Variable extends Thing implements IThingListSubscriber, IThingListPublisher {
    /**
     * The {@link List} with elements of type {@link UUID} containing IDs of prediction modelIds which are linked to this variable.
     */
    protected List<UUID> modelIds  = new ArrayList<>();

    /**
     * The {@link List} with elements of type {@link UUID} containing IDs of deviceIds which are linked to this variable.
     */
    protected List<UUID> deviceIds = new ArrayList<>();

    /**
     *  The {@link List} with elements of type {@link LinkEntry} which point to the variable's own API URIs.
     */
    protected List<LinkEntry> links          = new ArrayList<>();

    /**
     *  The {@link List} with elements of type {@link LinkEntry} which point to the {@link VariableList} API URIs.
     */
    protected List<LinkEntry> extLinks       = new ArrayList<>();

    /**
     *  The {@link List} with elements of type {@link LinkEntry} which point to the linked {@link AbstractPredictionModel}s' API URIs.
     */
    protected List<LinkEntry> modelLinks     = new ArrayList<>();

    /**
     *  The {@link List} with elements of type {@link LinkEntry} which point to the linked {@link AbstractDevice}s' API URIs.
     */
    protected List<LinkEntry> deviceLinks    = new ArrayList<>();

    /**
     *  The {@link List} with elements of type {@link LinkEntry} which point to the linked API URIs of {@code /api/vars/deviceIds/}.
     */
    protected List<LinkEntry> varDeviceLinks = new ArrayList<>();

    /**
     *  The {@link List} with elements of type {@link LinkEntry} which point to the linked API URIs of {@code /api/vars/modelIds/}.
     */
    protected List<LinkEntry> varModelLinks  = new ArrayList<>();

    /**
     * The list of observers of type {@link IThingListSubscriber}
     */
    protected List<IThingListSubscriber> observers = new ArrayList<>();

    /**
     *  Create a {@code Variable} with the given name and model and device IDs.
     * 
     * @param name the variable's name
     * @param modelIds the list of {@link AbstractPredicitonModel} IDs
     * @param deviceIds the list of {@link AbstractDevice} IDs
     */
    public Variable(String name, List<UUID> modelIds, List<UUID> deviceIds) {
        this.id   = UUID.randomUUID();
        this.name = name;
        this.modelIds.clear();
        this.modelIds.addAll(modelIds);
        this.deviceIds.clear();
        this.deviceIds.addAll(deviceIds);
    }

    /**
     * Set the variable's name.
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
        updateLinks();
    }

    /**
     * Replace/Set the list of model IDs.
     * 
     * @param modelIDs the {@link List} with elements of type {@link UUID} containing IDs of {@link AbstractPredictionModel}s.
     */
    public void setModelIds(List<UUID> modelIDs) {
        this.modelIds.clear();
        this.modelIds.addAll(modelIDs);
        updateModelLinks();
    }

    /**
     * Add a model ID to {@code modelIds}.
     * 
     * @param modelID the {@link UUID} of the {@link AbstractPredictionModel} that shall be added
     */
    public void addModel(UUID modelID) {
        if (!this.modelIds.contains(modelID)) {
            this.modelIds.add(modelID);
            updateModelLinks();
        }
    }
    
    /**
     * Return the model IDs.
     * 
     * @return the field {@code modelIds}
     */
    public List<UUID> getModelIds() {
        return this.modelIds;
    }

    /**
     * Return the model links.
     * 
     * @return the field {@code modelLinks}
     */
    public List<LinkEntry> getModelLinks() {
        return this.modelLinks;
    }

    /**
     * Clear the field {@code deviceIds}.
     */
    public void clearDeviceIds() {
       unregisterFromSubject();
       this.deviceIds.clear();
       updateDeviceLinks(); 
    }

    /**
     * Clear the field {@code modelIds}.
     */
    public void clearModelIds() {
        notifyObservers(new VariableModification(ModificationType.DELETION, this.id));
        this.modelIds.clear();
        updateVarModelLinks();
    }

    /**
     * Replace/Set the list of device IDs.
     * 
     * @param deviceIDs the {@link List} with elements of type {@link UUID} containing IDs of {@link AbstractDevice}s.
     */
    public void setDeviceIds(List<UUID> deviceIDs) {
        this.deviceIds.clear();
        this.deviceIds.addAll(deviceIDs);
        updateDeviceLinks();
        registerToSubject();
    }

    /**
     * Add a device ID to {@code deviceIds}.
     * 
     * @param deviceID the {@link UUID} of the {@link AbstractDevice} that shall be added
     */
    public void addDevice(UUID deviceID) {
        if (!this.deviceIds.contains(deviceID)) {
            this.deviceIds.add(deviceID);
            updateDeviceLinks();
            registerToSubject();
        }
    }

    /**
     * Return the list of device IDs.
     * 
     * @return the field {@code deviceIds}
     */
    public List<UUID> getDeviceIds() {
        return this.deviceIds;
    }

    /**
     * Return the list of device links.
     * 
     * @return the field {@code deviceLinks}
     */
    public List<LinkEntry> getDeviceLinks() {
        return this.deviceLinks;
    }

    /**
     * Return the list of variable links.
     * 
     * @return the field {@code links}
     */
    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    /**
     * Return all entries of {@code links} which have the label "selfStatic".
     * 
     * @return a {@link List} with elements of type {@link LinkEntry}
     */
    public List<LinkEntry> getSelfStaticLinks(String label) {
        List<LinkEntry> links = new ArrayList<>();
       
        for(LinkEntry link: getSelfLinks()) {
            if (link.getRelation().equals("selfStatic")) {
                links.add(new LinkEntry(label, link.getLink(), link.getAction(), link.getTypes()));
            }
        }

        return links;
    }

    /**
     * Return the {@link VariableList}s links.
     * 
     * @return the field {@code extLinks}
     */
    public List<LinkEntry> getExtLinks() {
        return this.extLinks;
    }

    /**
     * Return the links of linked devices.
     * 
     * @return the field {@code varDeviceLinks}
     */
    public List<LinkEntry> getVarDeviceLinks() {
        return this.varDeviceLinks;
    }

    /**
     * Return the links of linked models.
     * 
     * @return the field {@code varModelLinks}
     */
    public List<LinkEntry> getVarModelLinks() {
        return this.varModelLinks;
    }

    /**
     * Return all links.
     * 
     * @return the links of {@code links}, {@code extLinks}, {@code deviceLinks}, and {@code modelLinks}
     */
    public List<LinkEntry> getAllLinks() {
        List<LinkEntry> links = new ArrayList<>();
        links.addAll(getSelfLinks());
        links.addAll(getModelLinks());
        links.addAll(getDeviceLinks());
        links.addAll(getExtLinks());
        return links;
    }

    /**
     * Return the variable's display representation.
     * 
     * @return the {@link VariableDisplayRepresenatation} of this variable
     */
    public VariableDisplayRepresentation getDisplayRepresentation() {
        return new VariableDisplayRepresentation(this);
    }

    /**
     * Update all links.
     */
    public void updateLinks() {
        updateSelfLinks();
        updateDeviceLinks();
        updateModelLinks();
        updateExtLinks();
        updateVarDeviceLinks();
    }
    
    /**
     * Update the links of the field {@code links}.
     */
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

    /**
     * Update the links of the field {@code deviceLinks}.
     */
    protected void updateDeviceLinks() {
        if (deviceLinks != null || !deviceLinks.isEmpty()) {
            deviceLinks.clear();
        }

        for (UUID deviceId: deviceIds) {
            deviceLinks.addAll(ListService.getDeviceList().getThing(deviceId.toString()).getSelfStaticLinks("device"));
        }
    }

    /**
     * Update the links of the field {@code modelLinks}.
     */
    protected void updateModelLinks() {
        if (modelLinks != null || !modelLinks.isEmpty()) {
            modelLinks.clear();
        }

        for (UUID modelId: modelIds) {
            modelLinks.addAll(ListService.getPredictionModelList().getThing(modelId.toString()).getSelfStaticLinks("model"));
        }
    }

    /**
     * Update the links of the field {@code extLinks}.
     */
    protected void updateExtLinks() {
        if (extLinks != null || !extLinks.isEmpty()) {
            extLinks.clear();
        }

        extLinks.addAll(ListService.getVariableList().getLinks("variables"));
    }

    /**
     * Update the links of the field {@code varDeviceLinks}.
     */
    protected void updateVarDeviceLinks() {
        LinkEntry get    = new LinkEntry("deviceIds", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry("deviceIds", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.POST, List.of("application/json"));
        LinkEntry patch  = new LinkEntry("deviceIds", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.PATCH, List.of("application/json-patch+json"));
        LinkEntry delete = new LinkEntry("deviceIds", LinkBuilderService.getVariableDevicesLink(this.id.toString()), HttpAction.DELETE, List.of());


        if (varDeviceLinks != null || !varDeviceLinks.isEmpty()) {
            varDeviceLinks.clear();
        }

        if (varDeviceLinks.isEmpty()) {
            varDeviceLinks.addAll(List.of(get, post, patch));
        } else {
            varDeviceLinks.addAll(List.of(get, patch, delete));
        }
    }

    /**
     * Update the links of the field {@code varModelLinks}.
     */
    protected void updateVarModelLinks() {
        LinkEntry get    = new LinkEntry("modelIds", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry("modelIds", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.POST, List.of("application/json"));
        LinkEntry patch  = new LinkEntry("modelIds", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.PATCH, List.of("application/json-patch+json"));
        LinkEntry delete = new LinkEntry("modelIds", LinkBuilderService.getVariableModelsLink(this.id.toString()), HttpAction.DELETE, List.of());


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
        builder.append("deviceIds: "  + deviceIds + "\n");
        builder.append("modelIds:\t " + modelIds  + "\n");

        return builder.toString();
    }

    /**
     * Register as an observer at the {@link DeviceList}.
     */
    public void registerToSubject() {
        ListService.getDeviceList().attach(this);
    }

    /**
     * Unregister as an observer from the {@link DeviceList}.
     */
    public void unregisterFromSubject() {
        ListService.getDeviceList().detach(this);
    }

    /**
     * Implement {@link com.vs.foosh.api.model.misc.IThingListSubscriber#update(AbstractModification) update(AbstractModification)}.
     * 
     * If the list of devices was deleted, the list of device IDs is cleared.
     * All links are updated and the observers are notified about the recent change.
     * 
     * @param modification the {@link AbstractModification} describing the reason for notifying the {@link IThingListSubscriber}s
     */
    @Override
    public void update(AbstractModification modification) {
        if (modification.getModificationType() == ModificationType.DELETION) {
            deviceIds.clear();
        }

        updateLinks();

        notifyObservers(new VariableModification(ModificationType.DEPENDENCIES_CHANGED, this.id));
    }

    /**
     * Implement {@link com.vs.foosh.api.model.misc.IThingListPublisher#attach(IThingListSubscriber) attach(IThingListObserver)}.
     * The given observer is added to the list of observers.
     * 
     * @param observer the {@link IThingListSubscriber} to be added to {@code observers}
     */
    @Override
    public void attach(IThingListSubscriber observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            AbstractPredictionModel model = (AbstractPredictionModel) observer;
            modelIds.add(model.getId());
        }

        updateModelLinks();
    }

    /**
     * Implement {@link com.vs.foosh.api.model.misc.IThingListPublisher#detach(IThingListSubscriber) deatch(IThingListObserver)}.
     * The given observer is removed from the list of observers.
     * 
     * @param observer the {@link IThingListSubscriber} to be removed from {@code observers}
     */
    @Override
    public void detach(IThingListSubscriber observer) {
        observers.remove(observer);
        AbstractPredictionModel model = (AbstractPredictionModel) observer;
        modelIds.remove(model.getId());

        updateModelLinks();
    }

    /**
     * Implement {@link com.vs.foosh.api.model.misc.IThingListPublisher#notifyObservers(AbstractModification) notifyObservers(AbstractModification)}.
     * 
     * First, all observers are notified about the modification.
     * Then, the {@code observers} is cleared.
     * 
     * @param modification the {@link AbstractModification} describing the reason for notifying the {@link IThingListSubscriber}s
     */
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
