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

import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.device.DeviceNamePatchRequest;
import com.vs.foosh.api.model.misc.ReadSaveFileResult;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeCredentials;
import com.vs.foosh.api.exceptions.device.BatchDeviceNameException;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.ApplicationConfig;
import com.vs.foosh.api.services.HttpResponseBuilder;

@RequestMapping(value="/api/devices")
public abstract class AbstractDeviceController {

    //
    // Device Collection
    //

    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesGet() {
        return HttpResponseBuilder.buildResponse(
                new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDisplayListRepresentation()),
                DeviceList.getLinks("self"),
                HttpStatus.OK);
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPost(
            @RequestBody(required = false) SmartHomeCredentials credentials) {
        if (DeviceList.getInstance() == null || !DeviceList.getInstance().isEmpty()) {

            return HttpResponseBuilder.buildException(
                "There are already registered devices! Please use PUT/PATCH on /devices/ to update the list.",
                DeviceList.getLinks("self"),
                HttpStatus.CONFLICT);
        }

        FetchDeviceResponse apiResponse;
        ReadSaveFileResult<AbstractDevice> readResult = PersistentDataService.hasSavedDeviceList();
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

                PersistentDataService.saveDeviceList();
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

    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPut(
            @RequestBody(required=false) SmartHomeCredentials credentials) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/! Either use PATCH to update or DELETE and POST to replace the list of devices.",
                DeviceList.getLinks("self"));
    }

    @PatchMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPatch(@RequestBody List<DeviceNamePatchRequest> request) {
        if (patchBatchDeviceName(request)) {
            PersistentDataService.saveDeviceList();

            return HttpResponseBuilder.buildResponse(
                    new AbstractMap.SimpleEntry<String, Object>("devices", DeviceList.getDisplayListRepresentation()),
                    DeviceList.getLinks("self"),
                    HttpStatus.OK);
        } else {
            throw new BatchDeviceNameException();
        }
    }

    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesDelete() {
        DeviceList.clearDevices();

        PersistentDataService.deleteDeviceListSave();

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

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deviceGet(@PathVariable("id") String id) {
        AbstractDevice device = DeviceList.getDevice(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(device.getSelfLinks());
        links.addAll(device.getExtLinks());

        return HttpResponseBuilder.buildResponse(device, links, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePost(@PathVariable("id") String id) {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.POST, List.of("application/json")));

        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /devices/{id}! Please use POST on /devices/ instead.",
                links);
    }

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePut(@PathVariable("id") String id) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/{id}! Either use PATCH to update or DELETE and POST to replace a device.",
                DeviceList.getDevice(id).getSelfLinks());
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePatch(@PathVariable("id") String id, @RequestBody Map<String, String> requestBody) {
        UUID uuid;

        // Is the provided id a valid UUID?
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IdIsNoValidUUIDException(id);
        }

        // check whether there is a device with the given id
        DeviceList.checkIfIdIsPresent(id);

        // Is there a field called 'name'?
        if (requestBody.get("name") == null) {
            throw new DeviceNameIsNullException(uuid, requestBody);
        }

        String name = requestBody.get("name").toLowerCase();

        // Is this field non-empty?
        if (name.isEmpty() || name.equals("")) {
            throw new DeviceNameIsEmptyException(new DeviceNamePatchRequest(uuid, name));
        }
        
        
        if (patchDeviceName(new DeviceNamePatchRequest(uuid, name))) {
            PersistentDataService.saveDeviceList();

            AbstractDevice device = DeviceList.getDevice(uuid.toString());

            List<LinkEntry> links = new ArrayList<>();
            links.addAll(device.getSelfLinks());
            links.addAll(device.getExtLinks());

            return HttpResponseBuilder.buildResponse(device, links, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("Could not patch name for device '" + id + "' !'", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deviceDelete(@PathVariable("id") String id) {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilder.getDeviceListLink(), HttpAction.DELETE, List.of()));

        throw new HttpMappingNotAllowedException(
                "You cannot delete an individual device. You can only delete the entire collection with DELETE on /devices/ !",
                links);
    }

    private boolean patchDeviceName(DeviceNamePatchRequest request) {
        String name = request.getName().toLowerCase();
        UUID id = request.getId();

        // Does the field contain any letters, i.e., is it not empty?
        if (name.trim().isEmpty()) {
            throw new DeviceNameIsEmptyException(request);
        }

        // Is the name provided by the field unique?
        if (DeviceList.isUniqueName(name, id)) {
            DeviceList.getDevice(id.toString()).setName(name);

            return true;
        } else {
            throw new DeviceNameIsNotUniqueException(request);
        }
    }

    private boolean patchBatchDeviceName(List<DeviceNamePatchRequest> batchRequest) {
        List<AbstractDevice> oldDeviceList = DeviceList.getInstance();

        for(DeviceNamePatchRequest request: batchRequest) {
            if (!patchDeviceName(request)) {
                DeviceList.clearDevices();
                DeviceList.setDevices(oldDeviceList);
                return false;
            } 
        }

        return true;
    }
}
