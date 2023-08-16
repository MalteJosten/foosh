package com.vs.foosh.api.model.device;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A data type containing a {@link Map} of properties, defined by the smart home (API).
 * It is up to the developer to implement and use this class to store and process additional device data
 * provided by and retrieved from the external smart home system.
 * 
 * @apiNote The class implements the {@link Serializable} interface as saving and loading, i.e. JSON (de)serialization
 * with <a href="https://github.com/FasterXML/jackson">Jackson</a> requires this interface.
 */
public abstract class AbstractDeviceDescription implements Serializable {
    protected Map<String, Object> properties = new HashMap<>();

    /**
     * Empty constructor for used (de)serialization.
     */
    public AbstractDeviceDescription() {}

    /**
     * Create a new {@code AbstractDeviceDescription} using a {@link Map} with {@link String} keys and values of type
     * {@link Object}.
     * 
     * @apiNote The Map allows values of type {@link Object} to not limit the developers options in choosing a data type
     * best fitting his use case.
     * 
     * @param props a {@link Map} of {@link String}-{@link Object} pairs 
     */
    public AbstractDeviceDescription(Map<String, Object> props) {
        this.properties = props;
    }

    /**
     * Return the field {@code properties}.
     * 
     * @return the {@link Map} of {@link String}-{@link Object}
     */
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
