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

        JsonNode tagNode = description.get("tags");
        Iterator<JsonNode> tagIter = tagNode.elements();
        List<String> tags = new ArrayList<>();

        while (tagIter.hasNext()) {
            tags.add(tagIter.next().asText());
        }
        properties.put("tags", tags);

        JsonNode groupNode = description.get("groupNames");
        Iterator<JsonNode> groupIter = groupNode.elements();
        List<String> groups = new ArrayList<>();

        while (groupIter.hasNext()) {
            groups.add(groupIter.next().asText());
        }
        properties.put("groups", groups);
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }
}
