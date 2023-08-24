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
import com.vs.foosh.api.model.misc.IThingListSubscriber;
import com.vs.foosh.api.model.misc.IThingListPublisher;
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
 * It implements {@link IThingListPublisher} as it can be observed by objects implementing the {@link IThingListSubscriber}.
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 * 
 * 
 * It implements {@link IThingList}<{@link AbstractDevice}, {@link DeviceDisplayRepresentation}> to give the class capabilities of a list of {@link Thing}s.
 * It implements {@link Serializable} so that it can be (de)serialized for saving and loading into and from persistent storage.
 * 
 * @see com.vs.foosh.api.services.PersistentDataService#saveDeviceList()
 * @see com.vs.foosh.api.services.PersistentDataService#hasSavedDeviceList()
 */
public class DeviceList implements IThingListPublisher, IThingList<AbstractDevice, DeviceDisplayRepresentation>, Serializable {
    /**
     * The list containing all registered {@link AbstractDevice}s.
     */
    private List<AbstractDevice> devices;

    /**
     * The maximum number of tries to find a unique name.
     * 
     * @see #findUniqueName(DeviceNamePatchRequest)
     */
    private final int UNIQUE_NAME_TIMEOUT = 10;


    /**
     * The list of observers of type {@link IThingListSubscriber}
     */
    private List<IThingListSubscriber> observers = new ArrayList<>();


    /**
     * Create a {@code DeviceList} with an empty list of devices.
     */
    public DeviceList() {
        this.devices = new ArrayList<>();
    }

    /**
     * Set the list of devices.
     * 
     * @param deviceList the {@link List} with elements of type {@link AbstractDevice}
     */
    @Override
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
    @Override
    public List<AbstractDevice> getList() {
        return this.devices;
    }

    /**
     * Return a list of devices as things.
     * 
     * @return a {@link List} with elements of type {@link AbstractDevice} as a {@link Thing}
     */
    @Override
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
    @Override
    public void clearList() {
        notifyObservers(new ListModification(ModificationType.DELETION)); 
        this.devices.clear();
    }

    /**
     * Build and return the display representation of every device currently present in the {@code devices}.
     * 
     * @return a {@link List} with elements of type {@link DeviceDisplayRepresentation}
     */
    @Override
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
     * Search for a device in {@code devices} based on its {@code id} or {@code name}.
     * 
     * @param identifier the identifier to match each {@link AbstractDevice}'s {@code id} and {@code name} against
     * @return the first matched {@link AbstractDevice} or throw a {@link DeviceIdNotFoundException} if no {@link AbstractDevice}
     * with matching fields was found.
     */
    @Override
    public AbstractDevice getThing(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equalsIgnoreCase(identifier)) {
                return device;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    /**
     * Add a device to the list of currently registered devices.
     * The device is only added if its name would unique in the list,i.e., no other device has the same name.
     * 
     * A {@link DeviceNameIsNotUniqueException} is thrown, when the name is not unique.
     * 
     * @param device the {@link AbstractDevice} to add to {@code devices} 
     */
    @Override
    public void addThing(AbstractDevice device) {
        if (isValidName(device.getName(), device.getId())) {
            this.devices.add(device);
        } else {
            throw new DeviceNameIsNotUniqueException(device.getId(), device.getDeviceName());
        }
    }

    /**
     * Given a {@link String} identifier, delete a device from the list of devices.
     * 
     * @param identifier either the name or the uuid of the {@link AbstractDevice} that should be removed from {@code devices}
     */
    @Override
    public void deleteThing(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equalsIgnoreCase(identifier)) {
                getList().remove(device); 
            }
        }
    }

    /**
     * Check whether the provided {@code name} is valid.
     * A name is valid if
     *     - it is not a {@link UUID}
     *     - it is not used by any other {@link AbstractDevice} in the list of devices
     * 
     * If the name is is not unique, a {@link DeviceNameIsNotUniqueException} is thrown.
     * 
     * @param name the {@code name} of the {@link AbstractDevice} under test
     * @param uuid the {@code id} of the {@link AbstractDevice} under test
     * @return {@code true} if the name is unique
     */
    @Override
    public boolean isValidName(String name, UUID uuid) {
        try {
            // Check whether the name is a UUID
            UUID.fromString(name);
            return false;
        } catch (IllegalArgumentException e) {
            for (AbstractDevice d: this.devices) {
                // Check whether the name is already used
                if (d.getName().equalsIgnoreCase(name)) {
                    // If it's already used, check whether it's the same device
                    if (d.getId().equals(uuid)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Check whether the a device with the provided identifier is present in the list of devices.
     * 
     * A {@link DeviceIdNotFoundException} is thrown if no {@link AbstractDevice} with matching {@code name} or {@code id} is found.
     * 
     * @param identifier the {@code name} or {@code id} of the {@link AbstractDevice} in question
     */
    public void checkIfIdIsPresent(String identifier) {
        for (AbstractDevice device: getList()) {
            if (device.getId().toString().equals(identifier) || device.getName().equalsIgnoreCase(identifier)) {
                return;
            }
        }

        throw new DeviceIdNotFoundException(identifier);
    }

    /**
     * Check whether the given {@link DeviceNamePatchRequest} contains a valid (new) name.
     * 
     * If the name is not unique, try and find another unique one by appending incrementing numbers to the {@code name}.
     * 
     * If the {@code UNIQUE_NAME_TIMEOUT} is reached, i.e., no unique name could be constructed, a {@link CouldNotFindUniqueDeviceNameException} is thrown.
     * 
     * @param request the {@link DeviceNamePatchRequest} containing the new {@code name} and {@code id} of the {@link AbstractDevice} in question
     */
    public String findUniqueName(DeviceNamePatchRequest request) {
        StringBuilder name = new StringBuilder(request.name().toLowerCase());
        UUID id = request.uuid();

        // Does the field contain any letters, i.e., is it not empty?
        if (name.toString().trim().isEmpty()) {
            name.replace(0, name.length(), getThing(id.toString()).getDeviceName());
        }

        StringBuilder newName = new StringBuilder(name.toString());
        for (int i = 0; i < UNIQUE_NAME_TIMEOUT; i++) {
            // Is the name provided by the field unique or the same as the current name?
            if (isValidName(newName.toString(), id)) {
                return newName.toString();
            } else {
                newName.replace(0, newName.length(), name.toString() + (i+1));
            }
        }

        throw new CouldNotFindUniqueDeviceNameException(id, UNIQUE_NAME_TIMEOUT);
    }

    /**
     * Return the list of self links.
     * These include all currently available HTTP endpoints for {@code /api/devices/}.
     * 
     * @param label the label which is used to construct each {@link LinkEntry}
     * @return the list with elements of type {@link LinkEntry} of currently available HTTP endpoints for {@code /api/devices/}
     */
    @Override
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

    /**
     * Update the links of all currently registered devices and call {@link com.vs.foosh.api.model.device.AbstractDevice#setLinks() setLinks()}
     * for every {@link AbstractDevice} in {@code devices}.
     */
    @Override
    public void updateLinks() {
        for (AbstractDevice device: getList()) {
            device.setLinks();
        }
    }

    /**
     * Implement {@link com.vs.foosh.api.model.misc.IThingListSubject#attach(IThingListSubscriber) attach(IThingListObserver)}.
     * The given observer is added to the list of observers.
     * 
     * @param observer the {@link IThingListSubscriber} to be added to {@code observers}
     */
    @Override
    public void attach(IThingListSubscriber observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Implement {@link com.vs.foosh.api.model.misc.IThingListSubject#detach(IThingListSubscriber) deatch(IThingListObserver)}.
     * The given observer is removed from the list of observers.
     * 
     * @param observer the {@link IThingListSubscriber} to be removed from {@code observers}
     */
    @Override
    public void detach(IThingListSubscriber observer) {
        observers.remove(observer);
    }

    /**
     * Implement {@link com.vs.foosh.api.model.misc.IThingListSubject#notifyObservers(AbstractModification) notifyObservers(AbstractModification)}.
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceList >>\n");
        builder.append("Devices:   " + devices + "\n");
        builder.append("Observers: " + observers);

        return builder.toString();
    }
}
