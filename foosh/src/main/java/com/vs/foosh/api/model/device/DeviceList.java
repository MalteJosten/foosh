package com.vs.foosh.api.model.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.model.misc.IThingList;
import com.vs.foosh.api.model.misc.IThingListObserver;
import com.vs.foosh.api.model.misc.IThingListSubject;
import com.vs.foosh.api.model.misc.ListModification;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;

// TODO: @Override toString()
public class DeviceList implements IThingListSubject, IThingList<AbstractDevice, DeviceDisplayRepresentation>, Serializable {
    private List<AbstractDevice> devices;
    private final int UNIQUE_QUERY_NAME_TIMEOUT = 25;

    private List<IThingListObserver> observers = new ArrayList<>();

    public DeviceList() {
        this.devices = new ArrayList<>();
    }

    public void setList(List<AbstractDevice> deviceList) {
        if (devices != null) {
            clearList();
        }

        this.devices.addAll(deviceList);
    }

    public List<AbstractDevice> getList() {
        return this.devices;
    }

    public List<Thing> getAsThings() {
        List<Thing> things = new ArrayList<>();

        for (AbstractDevice device: this.devices) {
            things.add(device);
        }

        return things;
    }

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

    public void clearList() {
        notifyObservers(ListModification.DELETION);
        this.devices.clear();
    }

    public List<DeviceDisplayRepresentation> getDisplayListRepresentation() {
        List<DeviceDisplayRepresentation> displayRepresentation = new ArrayList<>();

        for(AbstractDevice device: getList()) {
            displayRepresentation.add(device.getDisplayRepresentation());
        }

        return displayRepresentation;
    }

    ///
    /// Let the client search for a device by
    ///   (1) it's ID,
    ///   (2) it's name
    ///
    public AbstractDevice getThing(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equals(identifier.toLowerCase().replace("%20", " "))) {
                return device;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    public void addThing(AbstractDevice device) {
        if (isUniqueName(device.getName(), device.getId())) {
            this.devices.add(device);
        } else {
            throw new DeviceNameIsNotUniqueException(new DeviceNamePatchRequest(device.getId(), device.getName()));
        }
    }

    public void deleteThing(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equals(identifier.toLowerCase().replace("%20", " "))) {
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
                if (d.getName().equals(name.toLowerCase().replace("%20", " "))) {
                    // If it's already used, check whether it's the same device.
                    if (d.getId().equals(id)) {
                        return true;
                    }

                    throw new DeviceNameIsNotUniqueException(new DeviceNamePatchRequest(id, name));
                }
            
            }
        }
        
        return true;
    }

    public void checkIfIdIsPresent(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equals(identifier.toLowerCase().replace("%20", " "))) {
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
        StringBuilder name = new StringBuilder(request.getName().toLowerCase());
        UUID id = request.getId();

        // Does the field contain any letters, i.e., is it not empty?
        if (name.toString().trim().isEmpty()) {
            name.replace(0, name.length(), getThing(id.toString()).getDeviceName());
        }

        for (int i = 0; i < UNIQUE_QUERY_NAME_TIMEOUT; i++) {
            // Is the name provided by the field unique or the same as the current name?
            if (isUniqueName(name.toString(), id)) {
                return name.toString();
            } else {
                name.replace(0, name.length(), getThing(id.toString()).getDeviceName() + (i+1));
            }
        }

        throw new CouldNotFindUniqueDeviceNameException(id, UNIQUE_QUERY_NAME_TIMEOUT);
    }

    public List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.POST, List.of("application/json"));
        LinkEntry patch  = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.PATCH, List.of("application/json"));
        LinkEntry delete = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.DELETE, List.of());

        if (getList().isEmpty() || getList().size() == 0) {
            return new ArrayList<>(List.of(get, post));
        } else {
            return new ArrayList<>(List.of(get, patch, delete));
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
    public void notifyObservers(ListModification modification) {
        for(IThingListObserver observer: observers) {
            observer.update(modification);
        }

        if (modification == ListModification.DELETION) {
            observers.clear();
        }
    }
}
