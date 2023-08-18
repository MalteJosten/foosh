package com.vs.foosh.api.model.misc;

/**
 * An interface providing needed capabilities for a publisher.
 * 
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 */
public interface IThingListPublisher {
    /**
     * This method should be called by a subscriber to subscribe to this publisher.
     * 
     * @param observer the {@link IThingListSubscriber} subscribing to this publisher.
     */
    public void attach(IThingListSubscriber observer);

    /**
     * This method should be called by a subscriber to unsubscribe from this publisher.
     * 
     * @param observer the {@link IThingListSubscriber} unsubscribing from this publisher.
     */
    public void detach(IThingListSubscriber observer);

    /**
     * This method should be called by the publisher he wants to notify his subscribers about
     * a modification.
     * 
     * @param modification the {@link AbstractModification} to transmit to the subscribers
     */
    public void notifyObservers(AbstractModification modification);
    
}
