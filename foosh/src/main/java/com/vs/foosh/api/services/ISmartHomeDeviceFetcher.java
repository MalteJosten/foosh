package com.vs.foosh.api.services;

import java.io.IOException;

import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeCredentials;

public interface ISmartHomeDeviceFetcher {
    public FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException;

    public FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials) throws ResourceAccessException, IOException;
}
