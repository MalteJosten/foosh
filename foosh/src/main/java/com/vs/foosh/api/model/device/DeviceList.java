package com.vs.foosh.api.model.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.IThingList;
import com.vs.foosh.api.model.misc.IThingListObserver;
import com.vs.foosh.api.model.misc.IThingListSubject;
import com.vs.foosh.api.model.misc.ListModification;
import com.vs.foosh.api.model.misc.ModificationType;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilderService;

/**
 * A container holding all currently registered {@link AbstractDevice}s with a bunch of functions allowing setting, retrieval, and modification of
 * the managed devices.
 * 
 * It implements {@link IThingListSubject} as it can be observed by objects implementing the {@link IThingListObserver}.
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 * 
 * It implements {@link IThingList}<{@link AbstractDevice}, {@link DeviceDisplayRepresentation}> to give the class capabilities of a list of {@link Thing}s.
 * It implements {@link Serializable} so that it can be (de)serialized for saving and loading into and from persistent storage.
 * 
 * @see com.vs.foosh.api.services.PersistentDataService#saveDeviceList()
 * @see com.vs.foosh.api.services.PersistentDataService#hasSavedDeviceList()
 */
public class DeviceList implements IThingListSubject, IThingList<AbstractDevice, DeviceDisplayRepresentation>, Serializable {

    /**
     * The list containing all registered {@link AbstractDevice}s.
     */
    private List<AbstractDevice> devices;

    /**
     * The maximum number of tries to find a unique name.
     * 
     * @see #findUniqueName(DeviceNamePatchRequest)
     */
    private final int UNIQUE_NAME_TIMEOUT = 25;


    /**
     * The list of observers of type {@link IThingListObserver}
     */
    private List<IThingListObserver> observers = new ArrayList<>();


    /**
     * Create a {@code DeviceList} with an empty list of devices.
     */
    public DeviceList() {
        this.devices = new ArrayList<>();
    }

    /**
     * Set the list of devices.
     * 
     * @param deviceList the {@link List} of with elements of type {@link AbstractDevice}
     */
    public void setList(List<AbstractDevice> deviceList) {
        if (devices != null) {
            clearList();
        }

        this.devices.addAll(deviceList);
    }

    /**
     * Return the list of devices.
     * 
     * @return a {@link List} with elements of type {@link AbstractDevice}
     */
    public List<AbstractDevice> getList() {
        return this.devices;
    }

    /**
     * Return a list of devices as things.
     * 
     * @return a {@link List} with elements of type {@link AbstractDevice} as a {@link Thing}
     */
    public List<Thing> getAsThings() {
        List<Thing> things = new ArrayList<>();

        for (AbstractDevice device: this.devices) {
            things.add(device);
        }

        return things;
    }

    /**
     * Return a list of devices which IDs ({@link UUID}) match one of the {@link UUID}s given by the list {@code identifier}.
     * 
     * @return a {@link List} with elements of type {@link AbstractDevice}
     */
    public List<AbstractDevice> getThingsById(List<UUID> identifiers) {
        List<AbstractDevice> results = new ArrayList<>();

        for(UUID identifier: identifiers) {
            for (AbstractDevice device: getList()) {
                if (device.getId().equals(identifier)) {
                    results.add(device);
                }
            }
        }

        return results;
    }

    /**
     * Clear the list of devices after notifying all observers of the upcoming modification.
     * 
     * @see #notifyObservers(AbstractModification)
     */
    public void clearList() {
        notifyObservers(new ListModification(ModificationType.DELETION)); 
        this.devices.clear();
    }

    /**
     * Build and return the display representation of every device currently present in the {@code devices}.
     * 
     * @return a {@link List} with elements of type {@link DeviceDisplayRepresentation}
     */
    public List<DeviceDisplayRepresentation> getDisplayListRepresentation() {
        List<DeviceDisplayRepresentation> displayRepresentation = new ArrayList<>();

        for(AbstractDevice device: getList()) {
            displayRepresentation.add(device.getDisplayRepresentation());
        }

        return displayRepresentation;
    }

    /**
     * Return {@code true} if {@code devices} is {@code null} or empty.
     * 
     * @return {@code true} if {@code devices} is {@code null} or empty.
     */
    public boolean isListEmpty() {
        return (devices == null || devices.isEmpty());
    }

    /**
     * Search a device in {@code devices} based on its {@code id} and {@code name}.
     * 
     * @param identifier the identifier to match each {@link AbstractDevice}'s {@code id} and {@code name} against
     * @return the first matched {@link AbstractDevice} or throw a {@link DeviceIdNotFoundException} if no {@link AbstractDevice}
     * with matching fields was found.
     */
    public AbstractDevice getThing(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equalsIgnoreCase(identifier)) {
                return device;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    public void addThing(AbstractDevice device) {
        if (isUniqueName(device.getName(), device.getId())) {
            this.devices.add(device);
        } else {
            throw new DeviceNameIsNotUniqueException(device.getId(), device.getDeviceName());
        }
    }

    public void deleteThing(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equalsIgnoreCase(identifier)) {
                getList().remove(device); 
            }
        }
    }

    public boolean isUniqueName(String name, UUID id) {
        // Check whether the provided 'name' could be an UUID.
        // Names in form of an UUID are disallowed.
        try {
            UUID.fromString(name);
            return false;
        } catch (IllegalArgumentException e) {
            for (AbstractDevice d: this.devices) {
                // Check whether the name is already used
                if (d.getName().equalsIgnoreCase(name)) {
                    // If it's already used, check whether it's the same device.
                    if (d.getId().equals(id)) {
                        return true;
                    }

                    throw new DeviceNameIsNotUniqueException(id, name);
                }
            
            }
        }
        
        return true;
    }

    public void checkIfIdIsPresent(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equalsIgnoreCase(identifier)) {
                return;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    ///
    /// Check whether the given request contains an unique (new) name.
    /// If the name is not unique, try and find another unique one by
    /// appending incrementing numbers to deviceName.
    ///
    public String findUniqueName(DeviceNamePatchRequest request) {
        StringBuilder name = new StringBuilder(request.name().toLowerCase());
        UUID id = request.id();

        // Does the field contain any letters, i.e., is it not empty?
        if (name.toString().trim().isEmpty()) {
            name.replace(0, name.length(), getThing(id.toString()).getDeviceName());
        }

        for (int i = 0; i < UNIQUE_NAME_TIMEOUT; i++) {
            // Is the name provided by the field unique or the same as the current name?
            if (isUniqueName(name.toString(), id)) {
                return name.toString();
            } else {
                name.replace(0, name.length(), getThing(id.toString()).getDeviceName() + (i+1));
            }
        }

        throw new CouldNotFindUniqueDeviceNameException(id, UNIQUE_NAME_TIMEOUT);
    }

    public List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilderService.getDeviceListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilderService.getDeviceListLink(), HttpAction.POST, List.of("application/json"));
        LinkEntry delete = new LinkEntry(label, LinkBuilderService.getDeviceListLink(), HttpAction.DELETE, List.of());

        if (getList().isEmpty() || getList().size() == 0) {
            return new ArrayList<>(List.of(get, post));
        } else {
            return new ArrayList<>(List.of(get, delete));
        }
    }

    public void updateLinks() {
        for (AbstractDevice device: getList()) {
            device.setLinks();
        }
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceList >>\n");
        builder.append("Devices:   " + devices + "\n");
        builder.append("Observers: " + observers);

        return builder.toString();
    }
}
