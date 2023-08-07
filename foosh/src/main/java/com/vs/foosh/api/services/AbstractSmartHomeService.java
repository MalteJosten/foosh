package com.vs.foosh.api.services;

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

import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.web.SmartHomeCredentials;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.model.web.SmartHomePostResult;

public abstract class AbstractSmartHomeService {
    public List<SmartHomePostResult> sendAndExecuteSmartHomeInstructions(List<SmartHomeInstruction> instructions) {
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

    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException;

    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials)
            throws ResourceAccessException, IOException;

}