package com.vs.foosh.helper;

import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;

public class ThingMock extends Thing {
    public ThingMock(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
}
