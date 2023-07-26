package com.vs.foosh.api.model.misc;

import com.vs.foosh.api.model.enums.ListModification;

public interface IThingListSubject {
    public void attach(IThingListObserver observer);
    public void detach(IThingListObserver observer);
    public void notifyObservers(ListModification modification);
    
}