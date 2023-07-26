package com.vs.foosh.api.model.misc;

import com.vs.foosh.api.model.enums.ListModification;

public interface IThingListObserver {
    public void update(ListModification modification); 
}
