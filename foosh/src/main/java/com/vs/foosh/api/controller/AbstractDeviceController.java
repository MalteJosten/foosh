package com.vs.foosh.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
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
import com.vs.foosh.api.model.HttpAction;
import com.vs.foosh.api.model.LinkEntry;
import com.vs.foosh.api.model.QueryNamePatchRequest;
import com.vs.foosh.api.model.ReadSaveFileResult;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.PersistentDeviceListService;
import com.vs.foosh.api.services.ApplicationConfig;
import com.vs.foosh.api.services.HttpResponseBuilder;

@RequestMapping(value="/api/")
public abstract class AbstractDeviceController {

    //
    // Device Collection
    //

    @GetMapping(value = "devices/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesGet() {
        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDisplayListRepresentation()),
                DeviceList.getLinks("self"),
                HttpStatus.OK);
    }

    @PostMapping(value = "devices/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPost(
            @RequestBody(required = false) SmartHomeCredentials credentials) {
        if (DeviceList.getInstance() == null || !DeviceList.getInstance().isEmpty()) {

            return HttpResponseBuilder.buildException(
                "There are already registered devices! Please use PUT/PATCH on /devices/ to update the list.",
                DeviceList.getLinks("self"),
                HttpStatus.CONFLICT);
        }

        FetchDeviceResponse apiResponse;
        ReadSaveFileResult readResult = PersistentDeviceListService.hasSavedDeviceList();
        if (readResult.getSuccess()) {
            DeviceList.setDevices(readResult.getData());
        } else {
            try {
                if (credentials == null) {
                    apiResponse = fetchDevicesFromSmartHomeAPI();
                } else {
                    apiResponse = fetchDevicesFromSmartHomeAPI(credentials);
                }

                DeviceList.setDevices(apiResponse.getDevices());
                DeviceList.updateDeviceLinks();

                PersistentDeviceListService.saveDeviceList();
            } catch (ResourceAccessException rAccessException) {
                throw new SmartHomeAccessException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/api/devices/");
            } catch (IOException ioException) {
                throw new SmartHomeIOException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/api/devices/");
            }
        }

        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDisplayListRepresentation()),
                DeviceList.getLinks("self"),
                HttpStatus.CREATED);
    }

    @PutMapping("devices/")
    public ResponseEntity<Object> devicesPut(
            @RequestBody(required=false) SmartHomeCredentials credentials) {
        List<AbstractDevice> old = DeviceList.getInstance();

        if (!DeviceList.getInstance().isEmpty()) {
            devicesDelete();
        }

        ResponseEntity<Object> postResult = devicesPost(credentials);
        if (postResult.getStatusCode() == HttpStatus.CREATED) {
            return postResult;
        } else {
            DeviceList.setDevices(old);

            return HttpResponseBuilder.buildResponse(
                    new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDisplayListRepresentation()),
                    DeviceList.getLinks("self"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("devices/")
    public ResponseEntity<Object> devicesPatch(@RequestBody List<QueryNamePatchRequest> request) {
        if (patchBatchDeviceQueryName(request)) {
            PersistentDeviceListService.saveDeviceList();

            return HttpResponseBuilder.buildResponse(
                    new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDisplayListRepresentation()),
                    DeviceList.getLinks("self"),
                    HttpStatus.OK);
        } else {
            throw new BatchQueryNameException();
        }
    }

    @DeleteMapping("devices/")
    public ResponseEntity<Object> devicesDelete() {
        DeviceList.clearDevices();

        PersistentDeviceListService.deleteDeviceListSave();

        List<LinkEntry> links = DeviceList.getLinks("self");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", DeviceList.getInstance());
        responseBody.put("links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI() throws ResourceAccessException, IOException;

    protected abstract FetchDeviceResponse fetchDevicesFromSmartHomeAPI(SmartHomeCredentials credentials)
            throws ResourceAccessException, IOException;

    //
    // Device
    //

    @GetMapping("devices/{id}")
    public ResponseEntity<Object> deviceGet(@PathVariable("id") String id) {
        AbstractDevice device = DeviceList.getDevice(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(device.getSelfLinks());
        links.addAll(device.getExtLinks());

        return HttpResponseBuilder.buildResponse(device, links, HttpStatus.OK);
    }

    @PostMapping("devices/{id}")
    public ResponseEntity<Object> devicePost(@PathVariable("id") String id) {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.POST, List.of("application/json")));

        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /devices/{id}! Please use POST on /devices/ instead.",
                links);
    }

    @PutMapping("devices/{id}")
    public ResponseEntity<Object> devicePut(@PathVariable("id") String id) {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.PUT, List.of("application/json")));

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/{id}! Please use PUT on /devices/ instead.",
                links);
    }

    @PatchMapping("devices/{id}")
    public ResponseEntity<Object> devicePatch(@PathVariable("id") String id, @RequestBody Map<String, String> requestBody) {
        UUID uuid;

        // Is the provided id a valid UUID?
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IdIsNoValidUUIDException(id);
        }

        // check whether there is a device with the given id
        AbstractDevice device = DeviceList.getDevice(id);

        // Is there a field called 'queryName'?
        if (requestBody.get("queryName") == null) {
            throw new QueryNameIsNullException(uuid, requestBody);
        }

        String queryName = requestBody.get("queryName").toLowerCase();

        // Is this field non-empty?
        if (queryName.isEmpty() || queryName.equals("")) {
            throw new QueryNameIsEmptyException(new QueryNamePatchRequest(uuid, queryName));
        }
        
        
        if (patchDeviceQueryName(new QueryNamePatchRequest(uuid, queryName))) {
            PersistentDeviceListService.saveDeviceList();

            // we need to get it one more time, since we changed its queryName
            device = DeviceList.getDevice(uuid.toString());

            List<LinkEntry> links = new ArrayList<>();
            links.addAll(device.getSelfLinks());
            links.addAll(device.getExtLinks());

            return HttpResponseBuilder.buildResponse(device, links, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("Could not patch queryName for device '" + id + "' !'", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("devices/{id}")
    public ResponseEntity<Object> deviceDelete(@PathVariable("id") String id) {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.DELETE, List.of()));

        throw new HttpMappingNotAllowedException(
                "You cannot delete an individual device. You can only delete the entire collection with DELETE on /devices/ !",
                links);
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
