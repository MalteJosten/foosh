package com.vs.foosh.api.services;

import java.io.IOException;

import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeCredentials;

public abstract class AbstractSmartHomeService {
    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException;

    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials)
            throws ResourceAccessException, IOException;

}