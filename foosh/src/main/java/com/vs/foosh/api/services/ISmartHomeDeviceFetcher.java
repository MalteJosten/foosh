package com.vs.foosh.api.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeDetails;

@Service
public interface ISmartHomeDeviceFetcher {
    public FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeDetails details) throws ResourceAccessException, IOException;
}
