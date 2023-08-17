package com.vs.foosh.api.model.device;

import java.util.List;

/**
 * A data component containing a list with elements of type {@link AbstractDevice} and a boolean indicator {@code success} if fetching was successful.
 */
public class FetchDeviceResponse {
    /**
     * A {@link List} with elements of type {@link AbstractDevice} containing the list of fetched devices.
     */
    private List<AbstractDevice> devices;

    /**
     * A boolean to indicate, if the fetching process was successful.
     */
    private boolean success;

    /**
     * Create a {@code FetchDeviceResponse} given a a list of devices and a success indicator.
     * 
     * @param devices the {@link List} with elements of type {@link AbstractDevices}
     * @param success a boolean value indicating whether the fetching process was successful or not
     */
    public FetchDeviceResponse(List<AbstractDevice> devices, boolean success) {
        this.devices = devices;
        this.success = success;
    }

    /**
     * Return the list of fetched devices.
     * 
     * @return the {@link List} with elements of type {@link AbstractDevice}
     */
    public List<AbstractDevice> getDevices() {
        return this.devices;
    }

    /**
     * Return {@code true} if the fetching process was successful.
     * Return {@code false} if the fetching process was unsuccessful.
     * 
     * @return the value of {@code success}
     */
    public boolean getSucess() {
        return this.success;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< FetchDeviceResponse >>\n");
        builder.append("Devices: " + devices + "\n");
        builder.append("Success: " + success);

        return builder.toString();
    }

}
