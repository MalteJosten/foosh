package com.vs.foosh.api.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDeviceDescription implements Serializable {
    protected Map<String, Object> properties = new HashMap<>();

    public AbstractDeviceDescription() {}

    public AbstractDeviceDescription(Map<String, Object> props) {
        this.properties = props;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }
}
