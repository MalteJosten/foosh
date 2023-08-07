package com.vs.foosh.api.model.misc;

public abstract class AbstractModification {
    protected ModificationType modificationType;

    public AbstractModification(ModificationType modificationType) {
        this.modificationType = modificationType;
    }
    
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
