package com.vs.foosh.api.model.misc;

import java.io.Serializable;
import java.util.UUID;

/**
 * A class to represent a variety of smart home "things".
 * Each {@code Thing} must have an unique identifier and a name.
 * 
 * It implements the interface {@link Serializable} so it can be (de)serialized for saving and loading into and from persistent storage.
 * 
 * @apiNote The fields {@code id} and {@code name} should be set in the subclass' constructor.
 */
public class Thing implements Serializable {    
    /**
     * The unique identifier of the {@code Thing}.
     */
    protected UUID id;

    /**
     * The name of the {@code Thing} as a {@link String}.
     */
    protected String name;


    /**
     * Creates an empty {@code Thing}.
     */
    public Thing() { }

    /**
     * Return the field {@code id}.
     * 
     * @return the field {@code id}
     */
    public UUID getId() {
        return this.id;
    }
    
    /**
     * Return the field {@code name}.
     * 
     * @return the field {@code name}
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< Thing >>\n");
        builder.append("ID   : " + id + "\n");
        builder.append("Name : " + name);

        return builder.toString();
    }
}
