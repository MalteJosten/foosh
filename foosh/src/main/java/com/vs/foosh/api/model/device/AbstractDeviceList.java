package com.vs.foosh.api.model.device;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.model.enums.ListModification;
import com.vs.foosh.api.model.misc.IThingListObserver;
import com.vs.foosh.api.model.misc.IThingListSubject;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;

public class AbstractDeviceList implements IThingListSubject{
    private static List<AbstractDevice> devices;
    private static final int UNIQUE_QUERY_NAME_TIMEOUT = 25;

    private static List<IThingListObserver> observers = new ArrayList<>();

    public static List<AbstractDevice> getInstance() {
        if (devices == null) {
            devices = new ArrayList<AbstractDevice>();
        }

        return devices;
    }

    public static void setDevices(List<AbstractDevice> deviceList) {
        if (devices != null) {
            clearDevices();
        }

        getInstance().addAll(deviceList);
    }

    public void pushDevice(AbstractDevice device) {
        if (isUniqueName(device.getName(), device.getId())) {
            getInstance().add(device);
        } else {
            throw new DeviceNameIsNotUniqueException(new DeviceNamePatchRequest(device.getId(), device.getName()));
        }
    }

    private static List<AbstractDevice> getDevices() {
        return getInstance();
    }

    public static List<AbstractDeviceDisplayRepresentation> getDisplayListRepresentation() {
        List<AbstractDeviceDisplayRepresentation> displayRepresentation = new ArrayList<>();

        for(AbstractDevice device: getDevices()) {
            displayRepresentation.add(new AbstractDeviceDisplayRepresentation(device, device.getSelfLinks()));
        }

        return displayRepresentation;
    }

    public static void clearDevices() {
        notifyObservers(ListModification.DELETION);
        getInstance().clear();
    }

    ///
    /// Let the client search for a device by
    ///   (1) it's ID,
    ///   (2) it's name
    ///
    public static AbstractDevice getDeviceById(String identifier) {
        for (AbstractDevice device: getDevices()) {
            if (device.getId().toString().equals(identifier) || device.getName().equals(identifier.toLowerCase())) {
                return device;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    public static List<AbstractDevice> getDevicesById(List<UUID> identifiers) {
        List<AbstractDevice> results = new ArrayList<>();

        for(UUID identifier: identifiers) {
            for (AbstractDevice device: getDevices()) {
                if (device.getId().equals(identifier)) {
                    results.add(device);
                }
            }
        }

        return results;
    }

    public static void checkIfIdIsPresent(String identifier) {
        for (AbstractDevice device: getDevices()) {
            if (device.getId().toString().equals(identifier) || device.getName().equals(identifier.toLowerCase())) {
                return;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    public static boolean isUniqueName(String name, UUID id) {
        // Check whether the provided 'name' could be an UUID.
        // Names in form of an UUID are disallowed.
        try {
            UUID.fromString(name);
            return false;
        } catch (IllegalArgumentException e) {
            for (AbstractDevice d: getInstance()) {
                // Check whether the name is already used
                if (d.getName().equals(name)) {
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

    ///
    /// Check whether the given request contains an unique (new) name.
    /// If the name is not unique, try and find another unique one by
    /// appending incrementing numbers to deviceName.
    ///
    public static String findUniqueName(DeviceNamePatchRequest request) {
        StringBuilder name = new StringBuilder(request.getName().toLowerCase());
        UUID id = request.getId();

        // Does the field contain any letters, i.e., is it not empty?
        if (name.toString().trim().isEmpty()) {
            name.replace(0, name.length(), getDeviceById(id.toString()).getDeviceName());
        }

        for (int i = 0; i < UNIQUE_QUERY_NAME_TIMEOUT; i++) {
            // Is the name provided by the field unique or the same as the current name?
            if (isUniqueName(name.toString(), id)) {
                return name.toString();
            } else {
                name.replace(0, name.length(), getDeviceById(id.toString()).getDeviceName() + (i+1));
            }
        }

        throw new CouldNotFindUniqueDeviceNameException(id, UNIQUE_QUERY_NAME_TIMEOUT);
    }

    public static List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.POST, List.of("application/json"));
        LinkEntry patch  = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.PATCH, List.of("application/json"));
        LinkEntry delete = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.DELETE, List.of());

        if (getDevices().isEmpty() || getDevices().size() == 0) {
            return new ArrayList<>(List.of(get, post));
        } else {
            return new ArrayList<>(List.of(get, patch, delete));
        }
    }

    public static void updateDeviceLinks() {
        for (AbstractDevice device: getDevices()) {
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
    }
}
