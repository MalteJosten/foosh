package com.vs.foosh.api.model.device;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractDeviceDescription implements Serializable {
    protected Map<String, Object> properties = new HashMap<>();

    public AbstractDeviceDescription() {}

    public AbstractDeviceDescription(Map<String, Object> props) {
        this.properties = props;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceDescription >>\n");
        builder.append("Properties:");
        for(Entry<String, Object> property: properties.entrySet()) {
            builder.append("\n\t" + property.getKey() + ": " + property.getValue());
        }

        return builder.toString();
    }
}
