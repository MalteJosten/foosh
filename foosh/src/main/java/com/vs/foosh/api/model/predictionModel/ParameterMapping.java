package com.vs.foosh.api.model.predictionModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParameterMapping implements Serializable {
    private String parameter;
    private String deviceId;

    public ParameterMapping(String parameter, String deviceId) {
        this.parameter = parameter;
        this.deviceId  = deviceId;
    }

    public String getParameter() {
        return this.parameter;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

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

    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder(prefix + "<< ParameterMapping >>\n");
        builder.append(prefix + "Parameter: " + parameter + "\n");
        builder.append(prefix + "Device-ID: " + deviceId);

        return builder.toString();
    }
}
