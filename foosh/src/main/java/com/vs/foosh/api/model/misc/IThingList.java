package com.vs.foosh.api.model.misc;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.web.LinkEntry;

/**
 * A generic interface used for giving classes the capabilities for creating, managing, and operating on a list with elements of type {@link Thing}.
 * 
 * @param T a (sub)class of {@link Thing}
 * @param V the corresponding DisplayRepresentation to {@code T}
 */
public interface IThingList<T extends Thing,V> {
    /**
     * Set the list of things.
     * 
     * @param thingList the {@link List} with elements of type {@code T}
     */
    public void setList(List<T> thingList);

    /**
     * Return the list of things.
     * 
     * @return a {@link List} with elements of type {@code T}
     */
    public List<T> getList();

    /**
     * Return a list of objects of type {@code T} as {@link Thing}s.
     * 
     * @return a {@link List} with elements of type {@code T} as a {@link Thing}
     */
    public List<Thing> getAsThings();

    /**
     * Clear the list of things.
     */
    public void clearList();

    /**
     * Build and return the display representation of every thing currently present in the list of things.
     * 
     * @return a {@link List} with elements of type {@code V}
     */
    public List<V> getDisplayListRepresentation();

    /**
     * Search for a device in the list of things based on its {@code id} and {@code name}.
     * 
     * @param identifier the identifier to match each {@link Thing}'s {@code id} and {@code name} against
     * @return the first matched {@link Thing}
     */
    public T getThing(String identifier);

    /**
     * Add a thing to the list of things.
     * The thing should only be added if its name would unique in the list,i.e., no other thing has the same name.
     * 
     * @param thing the thing that should be to add to the list of things
     */
    public void addThing(T thing);

    /**
     * Given a {@link String} identifier, delete a thing from the list of things.
     * 
     * @param identifier either the name or the uuid of the {@link Thing} that should be removed from the list of things
     */
    public void deleteThing(String identifier);

    /**
     * Check whether the provided {@code name} is valid.
     * A name is valid if
     *     - if is not a {@link UUID}
     *     - if it is not used by any other {@link Thing} in the list of devices
     * 
     * If the name is is not unique, an exception should be thrown.
     * 
     * @param name the {@code name} of the {@link Thing} under test
     * @param uuid the {@code id} of the {@link Thing} under test
     * @return {@code true} if the name is unique
     */
    public boolean isValidName(String name, UUID id);

    /**
     * Check whether the a thing with the provided identifier is present in the list of things.
     * 
     * An exception should be is thrown if no {@link Thing} with matching {@code name} or {@code id} is found.
     * 
     * @param identifier the {@code name} or {@code id} of the {@link Thing} in question
     */
    public void checkIfIdIsPresent(String identifier);

    /**
     * Return the list of self links.
     * These include all currently available HTTP endpoints for the corresponding route.
     * 
     * @param label the label which is used to construct each {@link LinkEntry}
     * @return the list with elements of type {@link LinkEntry} of currently available HTTP endpoints for the corresponding route
     */
    public List<LinkEntry> getLinks(String label);

    /**
     * Update the links of all currently registered things and call {@link com.vs.foosh.api.model.misc.Thing#setLinks() setLinks()}
     * for every {@link Thing} in the list of things.
     */
    public void updateLinks();

}
