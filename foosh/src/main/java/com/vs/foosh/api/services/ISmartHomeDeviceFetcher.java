package com.vs.foosh.api.services;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeCredentials;

public interface ISmartHomeDeviceFetcher {
    public static FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException {
        return new FetchDeviceResponse(new ArrayList<>(), false);
    }

    public static FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials) throws ResourceAccessException, IOException {
        return new FetchDeviceResponse(new ArrayList<>(), false);
    }
}
