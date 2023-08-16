package com.vs.foosh.api.model.device;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vs.foosh.api.model.web.LinkEntry;

/**
 * A data type containing a {@link DeviceResponseObject} and a {@link List} of {@link LinkEntry}
 * to be used in HTTP responses.
 */
public class DeviceDisplayRepresentation {
    /**
     * The {@link DeviceResponseObject} holding a "reduced" version of a device
     */
    private DeviceResponseObject device;

    /**
     * The list of {@link LinkEntry} that holds the relevant link information for the current respond
     */
    private List<LinkEntry> links;

    /**
     * Creates a {@code DeviceDisplayRepresentation} given an {@link AbstractDevice}.
     * 
     * @param device the {@link AbstractDevice} to construct the display representation from
     */
    public DeviceDisplayRepresentation(AbstractDevice device) {
        this.device = new DeviceResponseObject(device);
        this.links  = device.getSelfLinks();
    }

    /**
     * Returns the representation's response object.
     * 
     * @return the representation's {@link DeviceResponseObject}
     */
    public DeviceResponseObject getDevice() {
        return this.device;
    }

    /**
     * Return the list containigall relevant {@link LinkEntry} elements.
     * 
     * @apiNote The {@code @JsonProperty} annotation is used to rename the field to "_links" when deserializing the object into JSON.
     */
    @JsonProperty("_links")
    public List<LinkEntry> getLinks() {
        return this.links;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< DeviceDisplayRepresentation >>\n");
        builder.append("DeviceResponseObject: " + device + "\n");
        builder.append("Links: " + links);

        return builder.toString();
    }
}
