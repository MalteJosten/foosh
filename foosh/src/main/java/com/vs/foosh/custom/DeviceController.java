package com.vs.foosh.custom;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.vs.foosh.api.controller.AbstractDeviceController;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeCredentials;
import com.vs.foosh.api.services.ApplicationConfig;

@RestController
public class DeviceController extends AbstractDeviceController {

    @Override
    protected FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException {
        RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(5)).build();
        List<AbstractDevice> devices = new ArrayList<>();

        JsonNode response = restTemplate.getForObject(ApplicationConfig.getSmartHomeCredentials().getUri() + "rest/items", JsonNode.class);

        for (JsonNode node : response) {
            if (!node.get("state").asText().equals("NULL")) {
                Device device = new Device(node);
                devices.add(device);
            }
        }

        return new FetchDeviceResponse(devices, true);
    }

    /// For this scenario, no authentication is needed.
    /// Therefore we ignore any input and just forward the request to the non-credential-using method.
    @Override
    protected FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials) throws ResourceAccessException, IOException {
        return fetchDevicesFromSmartHomeAPI();
    }
    
}
