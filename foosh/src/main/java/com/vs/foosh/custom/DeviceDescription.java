package com.vs.foosh.custom;

import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.vs.foosh.api.model.device.AbstractDeviceDescription;

public class DeviceDescription extends AbstractDeviceDescription {
    public DeviceDescription(Map<String, Object> props) {
        super(props);
    }

    public DeviceDescription(JsonNode description) {
        properties.put("link",     description.get("link").asText());
        properties.put("state",    description.get("state").asText());
        properties.put("editable", description.get("editable").asBoolean());
        properties.put("type",     description.get("type").asText());
        properties.put("name",     description.get("name").asText());
        properties.put("label",    description.get("label").asText());
        properties.put("category", description.get("category").asText());
        
        setMap("tags", "tags", description);
        setMap("groupNames", "groups", description);
    }

    private void setMap(String sourceName, String destName, JsonNode description) {
        JsonNode node = description.get(sourceName);
        Iterator<JsonNode> iter = node.elements();
        List<String> collection = new ArrayList<>();

        while (iter.hasNext()) {
            collection.add(iter.next().asText());
        }
        properties.put(destName, collection);
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }
}
