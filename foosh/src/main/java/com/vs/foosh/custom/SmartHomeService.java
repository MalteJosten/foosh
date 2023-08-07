package com.vs.foosh.custom;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeCredentials;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.model.web.SmartHomePostResult;
import com.vs.foosh.api.services.ApplicationConfig;
import com.vs.foosh.api.services.ISmartHomeDeviceFetcher;
import com.vs.foosh.api.services.ISmartHomeInstructionExecutor;

public class SmartHomeService implements ISmartHomeDeviceFetcher, ISmartHomeInstructionExecutor {

    public static FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException {
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
    public static FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials) throws ResourceAccessException, IOException {
        return fetchDevicesFromSmartHomeAPI();
    }
    
    public static List<SmartHomePostResult> sendAndExecuteSmartHomeInstructions(List<SmartHomeInstruction> instructions) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        List<SmartHomePostResult> responses = new ArrayList<>();

        for (SmartHomeInstruction instruction : instructions) {
            HttpEntity<String> postRequest = new HttpEntity<String>(instruction.payload(), headers);
            RestTemplate restTemplate = new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofSeconds(5))
                    .setReadTimeout(Duration.ofSeconds(5)).build();
            ResponseEntity<Object> response = restTemplate.exchange(
                    instruction.deviceUri(),
                    HttpMethod.POST,
                    postRequest,
                    Object.class);

            responses.add(new SmartHomePostResult(instruction.index(), response.getStatusCode()));
        }

        return responses;
    }
    
}
