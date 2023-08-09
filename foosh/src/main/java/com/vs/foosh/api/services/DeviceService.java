package com.vs.foosh.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.device.PatchDeviceNameValidationData;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.FooSHPatchOperation;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeCredentials;

public class DeviceService {

    public static ResponseEntity<Object> getDevices() {
        return respondWithDevices(HttpStatus.OK);
    }

    public static ResponseEntity<Object> postDevices(SmartHomeCredentials credentials) {
        if (!ListService.getDeviceList().isListEmpty()) {
            String message = "There are already registered devices! Please use PUT/PATCH on /devices/ to update the list.";

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", message);
            responseBody.put("_links", ListService.getDeviceList().getLinks("self"));
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }

        fetchDevices(credentials);

        return respondWithDevices(HttpStatus.CREATED);
    }

    private static void fetchDevices(SmartHomeCredentials credentials) {
        FetchDeviceResponse apiResponse;

        try {
            if (credentials == null) {
                apiResponse = SmartHomeService.getSmartHomeDeviceFetcher().fetchDevicesFromSmartHomeAPI();
            } else {
                apiResponse = SmartHomeService.getSmartHomeDeviceFetcher().fetchDevicesFromSmartHomeAPI(credentials);
            }

            ListService.getDeviceList().setList(apiResponse.getDevices());
            ListService.getDeviceList().updateLinks();

            PersistentDataService.saveDeviceList();
        } catch (ResourceAccessException rAccessException) {
            throw new SmartHomeAccessException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/api/devices/");
        } catch (IOException ioException) {
            throw new SmartHomeIOException(ApplicationConfig.getSmartHomeCredentials().getUri() + "/api/devices/");
        }
    }

    public static ResponseEntity<Object> deleteDevices() {
        ListService.getDeviceList().clearList();

        PersistentDataService.deleteDeviceListSave();
        PersistentDataService.saveVariableList();

        return respondWithDevices(HttpStatus.OK);
    }

    private static ResponseEntity<Object> respondWithDevices(HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", ListService.getDeviceList().getDisplayListRepresentation());
        responseBody.put("_links", ListService.getDeviceList().getLinks("self"));

        return new ResponseEntity<>(responseBody, status);
    }

    public static ResponseEntity<Object> getDevice(String id) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        return respondWithDevice(device, HttpStatus.OK);
    }

    public static PatchDeviceNameValidationData validatePatchDeviceRequest(String id, Map<String, String> requestBody) {
        UUID uuid = IdService.isUuid(id).orElseThrow(() -> new IdIsNoValidUUIDException(id));

        // check whether there is a device with the given id
        ListService.getDeviceList().checkIfIdIsPresent(id);

        // Is there a field called 'name'?
        if (requestBody.get("name") == null) {
            throw new DeviceNameIsNullException(uuid, requestBody);
        }

        String name = requestBody.get("name").toLowerCase();

        // Is this field non-empty?
        if (name.isEmpty() || name.equals("")) {
            throw new DeviceNameIsEmptyException(uuid, name);
        }

        return new PatchDeviceNameValidationData(id, uuid, name);
    }

    public static ResponseEntity<Object> patchDevice(String id, List<Map<String, Object>> patchMappings) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, Object> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.REPLACE));
            patch.validateReplace(String.class);

            patches.add(patch);
        }    

        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("/name");
            if (!patch.isValidPath(pathSegments)) {
                throw new FooSHJsonPatchIllegalArgumentException(id, "You can only edit the field 'name'!");
            }

            patchDeviceName(id, (String) patch.getValue());
        }

        PersistentDataService.saveAll();
        
        return respondWithDevice(device, HttpStatus.OK);
    }

    private static boolean patchDeviceName(String id, String patchName) {
        UUID uuid = IdService.isUuid(id).get();

        // Does the field contain any letters, i.e., is it not empty?
        if (patchName.trim().isEmpty()) {
            throw new DeviceNameIsEmptyException(uuid, patchName);
        }

        // Is the name provided by the field unique?
        if (ListService.getDeviceList().isUniqueName(patchName, uuid)) {
            AbstractDevice device = ListService.getDeviceList().getThing(id.toString());
            device.setName(patchName);
            return true;
        } else {
            throw new DeviceNameIsNotUniqueException(uuid, patchName);
        }
    }

    private static ResponseEntity<Object> respondWithDevice(AbstractDevice device, HttpStatus status) {
            List<LinkEntry> links = new ArrayList<>();
            links.addAll(device.getSelfLinks());
            links.addAll(device.getExtLinks());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("device", device.getDisplayRepresentation().getDevice());
            responseBody.put("_links", links);

            return new ResponseEntity<>(responseBody, status);
    }

}
