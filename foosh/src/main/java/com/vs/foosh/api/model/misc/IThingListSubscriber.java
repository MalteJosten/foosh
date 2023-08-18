package com.vs.foosh.api.model.misc;

/**
 * An interface providing needed capabilities for an subscriber.
 * 
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 */
public interface IThingListSubscriber {
    /**
     * This method should be called by the publisher to inform the subscriber about changes.
     * 
     * @param modification the {@link AbstractModification} describing the reason for calling the method
     */
    public void update(AbstractModification modification); 
}
