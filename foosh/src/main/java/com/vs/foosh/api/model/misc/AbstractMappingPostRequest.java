package com.vs.foosh.api.model.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.predictionModel.ParameterMapping;

public abstract class AbstractMappingPostRequest {
    protected UUID thingId;
    protected List<ParameterMapping> mappings = new ArrayList<>();

    public AbstractMappingPostRequest() {}

    public AbstractMappingPostRequest(UUID thingId, List<ParameterMapping> mappings) {
        this.thingId  = thingId;
        this.mappings = mappings;
    }

    public abstract void validate(String parentId, List<UUID> validDeviceIds);

    public UUID getThingId() {
        return this.thingId;
    }

    public List<ParameterMapping> getMappings() {
        return this.mappings;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< MappingPostRequest >>\n");
        builder.append("Thing-ID: " + thingId + "\n");
        builder.append("Mappings:");

        for(ParameterMapping mapping: mappings) {
            builder.append("\n");
            builder.append(mapping.toString("\t"));
        }

        return builder.toString();
    }

    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder(prefix + "<< MappingPostRequest >>\n");
        builder.append(prefix + "Thing-ID:\t" + thingId + "\n");
        builder.append(prefix + "Mappings:");

        for(ParameterMapping mapping: mappings) {
            builder.append("\n");
            builder.append(prefix + mapping.toString(prefix + "\t"));
        }

        return builder.toString();
    }
}
