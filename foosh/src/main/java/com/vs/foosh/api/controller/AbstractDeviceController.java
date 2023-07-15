package com.vs.foosh.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.URI;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.vs.foosh.api.model.SmartHomeCredentials;
import com.vs.foosh.api.exceptions.*;
import com.vs.foosh.api.model.AbstractDevice;
import com.vs.foosh.api.model.DeviceList;
import com.vs.foosh.api.model.FetchDeviceResponse;
import com.vs.foosh.api.model.QueryNamePatchRequest;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ApplicationConfig;
import com.vs.foosh.api.services.HttpResponseBuilder;

public abstract class AbstractDeviceController {

    //
    // Device Collection
    //

    @GetMapping(value = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesGet() {
        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getDeviceListLink());

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDevices()),
                linkBlock,
                HttpStatus.OK);
    }

    // TODO: [BUG] After calling a second time, it gets weird. Look into it!
    @PostMapping(value = "/devices", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPost(
            @RequestBody(required = false) SmartHomeCredentials credentials) {
        FetchDeviceResponse apiResponse;

        try {
            if (credentials == null) {
                System.out.println("null");
                apiResponse = fetchDevicesFromSmartHomeAPI();
            } else {
                System.out.println("non null");
                apiResponse = fetchDevicesFromSmartHomeAPI(credentials);
            }

            DeviceList.setDevices(apiResponse.getDevices());

            Map<String, URI> linkBlock = new HashMap<>();
            linkBlock.put("self", LinkBuilder.getDeviceListLink());

            return HttpResponseBuilder.buildResponse(
                    new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDevices()),
                    linkBlock,
                    HttpStatus.OK);
        } catch (ResourceAccessException rAccessException) {
            throw new SmartHomeAccessException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/devices");
        } catch (IOException ioException) {
            throw new SmartHomeIOException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/devices");
        }
    }

    @PutMapping("/devices")
    public ResponseEntity<Object> devicesPut() {
        throw new HttpMappingNotAllowedException(
                "You can only update the devices list with POST!",
                Map.of("self", LinkBuilder.getDeviceListLink()));
    }

    @PatchMapping("/devices")
    public ResponseEntity<Object> devicesPatch(@RequestBody List<QueryNamePatchRequest> request) {
        if (patchBatchDeviceQueryName(request)) {
            Map<String, URI> linkBlock = new HashMap<>();
            linkBlock.put("self", LinkBuilder.getDeviceListLink());

            return HttpResponseBuilder.buildResponse(
                    new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDevices()),
                    linkBlock,
                    HttpStatus.OK);
        } else {
            throw new BatchQueryNameException();
        }
    }

    @DeleteMapping("/devices")
    public ResponseEntity<Object> devicesDelete() {
        DeviceList.clearDevices();

        Map<String, URI> linkBlock = new HashMap<>();
        linkBlock.put("self", LinkBuilder.getDeviceListLink());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", DeviceList.getDevices());
        responseBody.put("links", linkBlock);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException;

    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials)
            throws ResourceAccessException, IOException;

    //
    // Device
    //

    @GetMapping("/device/{id}")
    public ResponseEntity<Object> deviceGet(@PathVariable("id") String id) {
        AbstractDevice device = DeviceList.getDevice(id);

        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @PostMapping("/device/{id}")
    public ResponseEntity<Object> devicePost(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You can only create/replace a device with either POST or PATCH on /devices !",
                Map.of("devices", LinkBuilder.getDeviceListLink()));
    }

    @PutMapping("/device/{id}")
    public ResponseEntity<Object> devicePut(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You can only create/replace a device with either POST or PATCH on /devices !",
                Map.of("devices", LinkBuilder.getDeviceListLink()));
    }

    /// no empty string
    @PatchMapping("/device/{id}")
    public ResponseEntity<Object> devicePatch(@PathVariable("id") String id, @RequestBody Map<String, String> requestBody) {
        String queryName = requestBody.get("queryName").toLowerCase();
        UUID uuid;

        // Is the provided id a valid UUID?
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IdIsNoValidUUIDException(id);
        }

        // Is a field called 'queryName'?
        if (queryName == null) {
            throw new QueryNameIsNullException(uuid, requestBody);
        }
        
        
        if (patchDeviceQueryName(new QueryNamePatchRequest(uuid, queryName))) {
            return new ResponseEntity<>(DeviceList.getDevice(uuid.toString()), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("Could not patch queryName for device '" + id + "' !'", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/device/{id}")
    public ResponseEntity<Object> deviceDelete(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot delete an individual device. You can only delete the entire collection with DELETE on /devices !",
                Map.of("devices", LinkBuilder.getDeviceListLink()));
    }

    private boolean patchDeviceQueryName(QueryNamePatchRequest request) {
        String queryName = request.getQueryName().toLowerCase();
        UUID id = request.getId();

        // Does the field contain any letters, i.e., is it not empty?
        if (queryName.trim().isEmpty()) {
            throw new QueryNameIsEmptyException(request);
        }

        // Is the name provided by the field unique?
        if (DeviceList.isAUniqueQueryName(queryName, id)) {
            DeviceList.getDevice(id.toString()).setQueryName(queryName);

            return true;
        } else {
            throw new QueryNameIsNotUniqueException(request);
        }
    }

    private boolean patchBatchDeviceQueryName(List<QueryNamePatchRequest> batchRequest) {
        List<AbstractDevice> oldDeviceList = DeviceList.getInstance();

        for(QueryNamePatchRequest request: batchRequest) {
            if (!patchDeviceQueryName(request)) {
                DeviceList.clearDevices();
                DeviceList.setDevices(oldDeviceList);
                return false;
            } 
        }

        return true;
    }
}
