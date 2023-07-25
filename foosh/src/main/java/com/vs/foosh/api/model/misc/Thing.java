package com.vs.foosh.api.model.misc;

import java.util.UUID;

public class Thing {
    protected final UUID id;
    protected String name;

    public Thing() {
        this.id = UUID.randomUUID();
    }

    public Thing(String name) {
        this.id   = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
