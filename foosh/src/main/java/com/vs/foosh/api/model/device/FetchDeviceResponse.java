package com.vs.foosh.api.model.device;

import java.util.List;

public class FetchDeviceResponse {
    private String message;
    private List<AbstractDevice> devices;
    private boolean success;

    public FetchDeviceResponse( List<AbstractDevice> devices, boolean success) {
        this.devices = devices;
        this.success = success;
    }

    public FetchDeviceResponse(String message, List<AbstractDevice> devices, boolean success) {
        this.message = message;
        this.devices = devices;
        this.success = success;
    }

    public String getMessage() { return this.message; }

    public List<AbstractDevice> getDevices() { return this.devices; }

    public boolean getSucess() { return this.success; }
}
