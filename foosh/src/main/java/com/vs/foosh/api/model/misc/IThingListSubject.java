package com.vs.foosh.api.model.misc;

public interface IThingListSubject {
    public void attach(IThingListObserver observer);
    public void detach(IThingListObserver observer);
    public void notifyObservers(AbstractModification modification);
    
}
