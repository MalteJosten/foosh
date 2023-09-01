package com.vs.foosh.api.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeDetails;

/**
 * An interface and {@link Service} that should provide the functionality of fetching a list of smart home devices from a smart home API.
 */
@Service
public interface ISmartHomeDeviceFetcher {
    /**
     * Given some {@link SmartHomeDetails}, a list of smart home devices should be retrieved from the smart home API.
     * 
     * @param details the {@link SmartHomeDetails} necessary to use the smart home API
     * @return the {@link FetchDeviceResponse} containing the result of the smart home API interaction
     */
    public FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeDetails details) throws ResourceAccessException, IOException;
}
