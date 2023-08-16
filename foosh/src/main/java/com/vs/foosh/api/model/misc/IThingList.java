package com.vs.foosh.api.model.misc;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.web.LinkEntry;

public interface IThingList<T,V> {
    public void setList(List<T> thingList);
    public List<T> getList();
    public List<Thing> getAsThings();
    public void clearList();
    public List<V> getDisplayListRepresentation();

    public T getThing(String identifier);
    public void addThing(T thing);
    public void deleteThing(String identifier);

    public boolean isValidName(String name, UUID id);
    public void checkIfIdIsPresent(String identifier);

    public List<LinkEntry> getLinks(String label);
    public void updateLinks();

}
