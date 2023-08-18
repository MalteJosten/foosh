package com.vs.foosh.api.model.misc;

/**
 * A subclass of {@link AbstractModification} to be used by {@link IThingList}s.
 */
public class ListModification extends AbstractModification {

    /**
     * Create a {@code ListModification}, given a {@link ModificationType}.
     * 
     * @param modificationType the {@link ModificationType} that describes the list modification
     */
    public ListModification(ModificationType modificationType) {
        super(modificationType);
    }
}
