package com.vs.foosh.api.model.misc;

/**
 * A container for a {@link ModificationType}.
 * It is used for indicating the type of change/modification when communicating with an observer.
 * 
 * @see com.vs.foosh.api.model.misc.IThingListObserver
 * @see com.vs.foosh.api.model.misc.IThingListSubject
 */
public abstract class AbstractModification {
    /**
     * The {@link ModificationType} of this modification.
     */
    protected ModificationType modificationType;

    /**
     * Create a new {@code AbstractModification} with the given {@code modificationType}.
     * 
     * @param modificationType the {@link ModificationType}
     */
    public AbstractModification(ModificationType modificationType) {
        this.modificationType = modificationType;
    }
    
    /**
     * Return the modification type.
     * 
     * @return the {@code modificationType} as {@link ModificationType}
     */
    public ModificationType getModificationType() {
        return this.modificationType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< Modification >>\n");
        builder.append("ModificationType: " + modificationType);

        return builder.toString();
    }
}
