package com.vs.foosh.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.CouldNotFindUniqueQueryNameException;
import com.vs.foosh.api.exceptions.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.QueryNameIsNotUniqueException;
import com.vs.foosh.api.services.LinkBuilder;

public class DeviceList {
    private static List<AbstractDevice> devices;
    private static final int UNIQUE_QUERY_NAME_TIMEOUT = 25;

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
        if (isAUniqueQueryName(device.getQueryName(), device.getId())) {
            getInstance().add(device);
        } else {
            throw new QueryNameIsNotUniqueException(new QueryNamePatchRequest(device.getId(), device.getQueryName()));
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
        getInstance().clear();
    }

    ///
    /// Let the client search for a device by
    ///   (1) it's ID,
    ///   (2) it's queryName
    ///
    public static AbstractDevice getDevice(String identifier) {
        for (AbstractDevice device: getDevices()) {
            if (device.getId().toString().equals(identifier) || device.getQueryName().equals(identifier.toLowerCase())) {
                return device;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    public static boolean isAUniqueQueryName(String name, UUID id) {
        for (AbstractDevice d: getInstance()) {
            // Check whether the queryName 'name' is already used.
            if (d.getQueryName().equals(name)) {
                // If it's already used, check whether it's the same device.
                if (d.getId() == id) {
                    return true;
                }

                return false;
            }
        }
        
        return true;
    }

    ///
    /// Check whether the given request contains an unique (new) queryName.
    /// If the queryName is not unique, try and find another unique one by
    /// appending incrementing numbers to deviceName.
    ///
    public static String findUniqueQueryName(QueryNamePatchRequest request) {
        StringBuilder queryName = new StringBuilder(request.getQueryName().toLowerCase());
        UUID id = request.getId();

        // Does the field contain any letters, i.e., is it not empty?
        if (queryName.toString().trim().isEmpty()) {
            queryName.replace(0, queryName.length(), getDevice(id.toString()).getDeviceName());
        }

        for (int i = 0; i < UNIQUE_QUERY_NAME_TIMEOUT; i++) {
            // Is the name provided by the field unique or the same as the current queryName?
            if (isAUniqueQueryName(queryName.toString(), id)) {
                return queryName.toString();
            } else {
                queryName.replace(0, queryName.length(), getDevice(id.toString()).getDeviceName() + (i+1));
            }
        }

        throw new CouldNotFindUniqueQueryNameException(id, UNIQUE_QUERY_NAME_TIMEOUT);
    }

    public static List<LinkEntry> getLinks(String label) {
        LinkEntry get    = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.GET, List.of());
        LinkEntry post   = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.POST, List.of("application/json"));
        LinkEntry put    = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.PUT, List.of("application/json"));
        LinkEntry patch  = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.PATCH, List.of("application/json"));
        LinkEntry delete = new LinkEntry(label, LinkBuilder.getDeviceListLink(), HttpAction.DELETE, List.of());

        if (getDevices().isEmpty() || getDevices().size() == 0) {
            return new ArrayList<>(List.of(get, put, post));
        } else {
            return new ArrayList<>(List.of(get, put, patch, delete));
        }
    }

    public static void updateDeviceLinks() {
        for (AbstractDevice device: getDevices()) {
            device.setLinks();
        }
    }

}
