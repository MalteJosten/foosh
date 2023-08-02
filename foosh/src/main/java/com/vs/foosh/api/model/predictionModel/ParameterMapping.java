package com.vs.foosh.api.model.predictionModel;

public class ParameterMapping {
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
