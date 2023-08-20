package com.vs.foosh.api.model.predictionModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A class storing the mapping of a prediction model parameter onto a smart home device.
 * @see {@link com.vs.foosh.api.model.predictionModel.AbstractPredictionModel}
 * @see {@link com.vs.foosh.api.model.device.AbstractDevice}
 * 
 * It implements the interface {@link Serializable} so it can be (de)serialized for saving and loading into and from persistent storage.
 */
public class ParameterMapping implements Serializable {
    /**
     * The name of the parameter.
     */
    private String parameter;

    /**
     * The {@link UUID} of the linked {@link AbstractDevice}.  
     */
    private String deviceId;

    /**
     * Create a parameter mapping.
     * 
     * @param parameter the name of the parameter 
     * @param deviceId the {@link UUID} of the device as a {@link String}
     */
    public ParameterMapping(String parameter, String deviceId) {
        this.parameter = parameter;
        this.deviceId  = deviceId;
    }

    /**
     * Return the field {@code parameter}.
     * 
     * @return the field {@code parameter}
     */
    public String getParameter() {
        return this.parameter;
    }

    /**
     * Return the field {@code deviceId}.
     * 
     * @return the field {@code deviceId}
     */
    public String getDeviceId() {
        return this.deviceId;
    }

    /**
     * Return the mapping between {@code parameter} and {@code deviceId} ({@link AbstractDevice}) as a {@link Map}.
     * 
     * {@link @JsonIgnore} so this method is ignored when (de)serializing.
     * 
     * @return a {@link Map} with one entry
     */
    @JsonIgnore
    public Map<String, String> getAsHashMap() {
        Map<String, String> map = new HashMap<>();
        map.put("parameter", this.parameter);
        map.put("deviceId", this.deviceId);

        return map;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< ParameterMapping >>\n");
        builder.append("Parameter: " + parameter + "\n");
        builder.append("Device-ID: " + deviceId);

        return builder.toString();
    }

    /**
     * A custom {@code toString} method where a prefix preceds the String representation.
     * 
     * @return the {@link String} representation of {@code ParameterMapping} with a prefix
     */
    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder(prefix + "<< ParameterMapping >>\n");
        builder.append(prefix + "Parameter: " + parameter + "\n");
        builder.append(prefix + "Device-ID: " + deviceId);

        return builder.toString();
    }
}
