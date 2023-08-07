package com.vs.foosh.api.model.misc;

import java.io.Serializable;
import java.util.UUID;

public class Thing implements Serializable {
    protected UUID id;
    protected String name;

    public Thing() { }

    public UUID getId() {
        return this.id;
    }

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
