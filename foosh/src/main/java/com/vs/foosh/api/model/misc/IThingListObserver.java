package com.vs.foosh.api.model.misc;

/**
 * An interface providing needed capabilities for an observer.
 * 
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 */
public interface IThingListObserver {
    /**
     * This method should be called by the subject to inform the observer about changes.
     * 
     * @param modification the {@link AbstractModification} describing the reason for calling the method
     */
    public void update(AbstractModification modification); 
}
