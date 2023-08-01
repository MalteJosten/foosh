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
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.ApplicationConfig;

// TODO: Extract logic into dedicated service(s)
@RequestMapping(value="/api/devices")
public abstract class AbstractDeviceController {

    //
    // Device Collection
    //

    // TODO: Implement Paging
    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesGet() {
        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("devices", ListService.getAbstractDeviceList().getDisplayListRepresentation());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getAbstractDeviceList().getLinks("self"));

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // TODO: Implement Paging
    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPost(
            @RequestBody(required = false) SmartHomeCredentials credentials) {
        if (ListService.getAbstractDeviceList().getList() == null || !ListService.getAbstractDeviceList().getList().isEmpty()) {

            String message = "There are already registered devices! Please use PUT/PATCH on /devices/ to update the list.";
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", message);
            responseBody.put("_links", ListService.getAbstractDeviceList().getLinks("self"));
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }

        FetchDeviceResponse apiResponse;
        ReadSaveFileResult<DeviceList> readResult = PersistentDataService.hasSavedDeviceList();
        if (readResult.getSuccess()) {
            ListService.setAbstractDeviceList(readResult.getData());
        } else {
            try {
                if (credentials == null) {
                    apiResponse = fetchDevicesFromSmartHomeAPI();
                } else {
                    apiResponse = fetchDevicesFromSmartHomeAPI(credentials);
                }

                ListService.getAbstractDeviceList().setList(apiResponse.getDevices());
                ListService.getAbstractDeviceList().updateLinks();

                PersistentDataService.saveDeviceList();
            } catch (ResourceAccessException rAccessException) {
                throw new SmartHomeAccessException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/api/devices/");
            } catch (IOException ioException) {
                throw new SmartHomeIOException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/api/devices/");
            }
        }

        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("devices", ListService.getAbstractDeviceList().getDisplayListRepresentation());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getAbstractDeviceList().getLinks("self"));
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPut(
            @RequestBody(required=false) SmartHomeCredentials credentials) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/! Use DELETE and POST to replace the list of devices.",
                ListService.getAbstractDeviceList().getLinks("self"));
    }

    // TODO: Allow patching? What is the correct path? How do we address the device we want to update, since we have no index but only its ID?
    @PatchMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPatch(@RequestBody List<DeviceNamePatchRequest> request) {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on /devices/! Either use PATCH on /devices/{id} to update the device's name or DELETE and POST to replace the list of devices.",
                ListService.getAbstractDeviceList().getLinks("self"));
    }

    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesDelete() {
        ListService.getAbstractDeviceList().clearList();

        PersistentDataService.deleteDeviceListSave();
        PersistentDataService.saveVariableList();

        List<LinkEntry> links = ListService.getAbstractDeviceList().getLinks("self");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", ListService.getAbstractDeviceList().getDisplayListRepresentation());
        responseBody.put("_links", links);

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
        AbstractDevice device = ListService.getAbstractDeviceList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(device.getSelfLinks());
        links.addAll(device.getExtLinks());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("device", device.getDisplayRepresentation().getDevice());
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}",
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
                ListService.getAbstractDeviceList().getThing(id).getSelfLinks());
    }

    // TODO: Implement custom Json Patch
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePatch(@PathVariable("id") String id, @RequestBody Map<String, String> requestBody) {
        UUID uuid;

        // TODO: Use IdService
        // Is the provided id a valid UUID?
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IdIsNoValidUUIDException(id);
        }

        // check whether there is a device with the given id
        ListService.getAbstractDeviceList().checkIfIdIsPresent(id);

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

            AbstractDevice device = ListService.getAbstractDeviceList().getThing(uuid.toString());
            List<LinkEntry> links = new ArrayList<>();
            links.addAll(device.getSelfLinks());
            links.addAll(device.getExtLinks());
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("device", device.getDisplayRepresentation().getDevice());
            responseBody.put("_links", links);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

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
        if (ListService.getAbstractDeviceList().isUniqueName(name, id)) {
            ListService.getAbstractDeviceList().getThing(id.toString()).setName(name);

            return true;
        } else {
            throw new DeviceNameIsNotUniqueException(request);
        }
    }

}
