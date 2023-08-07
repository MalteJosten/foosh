package com.vs.foosh.api.services;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNullException;
import com.vs.foosh.api.exceptions.misc.IdIsNoValidUUIDException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.device.DeviceNamePatchRequest;
import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.device.PatchDeviceNameValidationData;
import com.vs.foosh.api.model.misc.ReadSaveFileResult;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeCredentials;
import com.vs.foosh.custom.SmartHomeService;

public class DeviceService {
    private static SmartHomeService smartHomeService = new SmartHomeService();    

    public static ResponseEntity<Object> getDevices() {
        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("devices", ListService.getDeviceList().getDisplayListRepresentation());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getDeviceList().getLinks("self"));

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public static ResponseEntity<Object> postDevices(SmartHomeCredentials credentials) {
        if (ListService.getDeviceList().getList() == null || !ListService.getDeviceList().getList().isEmpty()) {

            String message = "There are already registered devices! Please use PUT/PATCH on /devices/ to update the list.";
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", message);
            responseBody.put("_links", ListService.getDeviceList().getLinks("self"));
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }

        FetchDeviceResponse apiResponse;
        ReadSaveFileResult<DeviceList> readResult = PersistentDataService.hasSavedDeviceList();
        if (readResult.getSuccess()) {
            ListService.setDeviceList(readResult.getData());
        } else {
            try {
                if (credentials == null) {
                    apiResponse = smartHomeService.fetchDevicesFromSmartHomeAPI();
                } else {
                    apiResponse = smartHomeService.fetchDevicesFromSmartHomeAPI(credentials);
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

        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("devices", ListService.getDeviceList().getDisplayListRepresentation());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getDeviceList().getLinks("self"));
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    public static ResponseEntity<Object> deleteDevices() {
        ListService.getDeviceList().clearList();

        PersistentDataService.deleteDeviceListSave();
        PersistentDataService.saveVariableList();

        List<LinkEntry> links = ListService.getDeviceList().getLinks("self");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", ListService.getDeviceList().getDisplayListRepresentation());
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public static ResponseEntity<Object> getDevice(String id) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(device.getSelfLinks());
        links.addAll(device.getExtLinks());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("device", device.getDisplayRepresentation().getDevice());
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);

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

    public static ResponseEntity<Object> patchDevice(PatchDeviceNameValidationData validationData) {
        if (patchDeviceName(new DeviceNamePatchRequest(validationData.uuid(), validationData.name()))) {
            PersistentDataService.saveDeviceList();

            AbstractDevice device = ListService.getDeviceList().getThing(validationData.uuid().toString());
            List<LinkEntry> links = new ArrayList<>();
            links.addAll(device.getSelfLinks());
            links.addAll(device.getExtLinks());
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("device", device.getDisplayRepresentation().getDevice());
            responseBody.put("_links", links);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } else {
            return new ResponseEntity<Object>("Could not patch name for device '" + validationData.identifier().replace(" ", "%20") +  "' !'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private static boolean patchDeviceName(DeviceNamePatchRequest request) {
        String name = request.getName().toLowerCase();
        UUID id     = request.getId();

        // Does the field contain any letters, i.e., is it not empty?
        if (name.trim().isEmpty()) {
            throw new DeviceNameIsEmptyException(request.getId(), request.getName());
        }

        // Is the name provided by the field unique?
        if (ListService.getDeviceList().isUniqueName(name, id)) {
            ListService.getDeviceList().getThing(id.toString()).setName(name);

            return true;
        } else {
            throw new DeviceNameIsNotUniqueException(request);
        }
    }
}
