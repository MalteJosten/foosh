package com.vs.foosh.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.vs.foosh.api.ApplicationConfig;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalPathException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsEmptyException;
import com.vs.foosh.api.exceptions.device.DeviceNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeAccessException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeIOException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.FetchDeviceResponse;
import com.vs.foosh.api.model.misc.ThingType;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.FooSHPatchOperation;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeDetails;
import com.vs.foosh.api.services.helpers.IdService;
import com.vs.foosh.api.services.helpers.ListService;

/**
 * A {@link Service} that provides functionalities for accessing and modifying (elements of) the {@link DeviceList}.
 */
@Service
public class DeviceService {

    /**
     * Return the contents of {@link DeviceList}.
     * 
     * @return the contents of {@link DeviceList} as a {@link ResponseEntity}
     */
    public static ResponseEntity<Object> getDevices() {
        return respondWithDevices(HttpStatus.OK);
    }

    /**
     * Given a set of {@link SmartHomeDetails}, fetch the list of registered smart devices from the smart home API.
     * 
     * If {@link DeviceList} already contains {@link AbstractDevice}s, a corresponding response is constructed and the list of devices is not fetched.
     * 
     * @param details the {@link SmartHomeDetails}
     * 
     * @return the response as a {@link ResponseEntity}
     */
    public static ResponseEntity<Object> postDevices(SmartHomeDetails details) {
        if (!ListService.getDeviceList().isListEmpty()) {
            String message = "There are already registered devices! Please use PATCH on /devices/ to update the list.";

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", message);
            responseBody.put("_links", ListService.getDeviceList().getLinks("self"));
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }

        fetchDevices(details);

        return respondWithDevices(HttpStatus.CREATED);
    }

    /**
     * Fetch the list of smart devices from the smart home API.
     * 
     * @param details the {@link SmartHomeDetails} which might be used to connect to the smart home API and retrieve the list of devices
     */
    private static void fetchDevices(SmartHomeDetails details) {
        FetchDeviceResponse apiResponse;

        try {
            apiResponse = SmartHomeService.getSmartHomeDeviceFetcher().fetchDevicesFromSmartHomeAPI(details);

            ListService.getDeviceList().setList(apiResponse.getDevices());
            ListService.getDeviceList().updateLinks();

            PersistentDataService.saveDeviceList();
        } catch (ResourceAccessException rAccessException) {
            throw new SmartHomeAccessException(ApplicationConfig.getSmartHomeDetails().getUri());
        } catch (IOException ioException) {
            throw new SmartHomeIOException(ApplicationConfig.getSmartHomeDetails().getUri());
        }
    }

    /**
     * Delete the contents of {@link DeviceList}, delete (and backup) the local save file and respond with {@code 200 OK}.
     * 
     * @return the contents of {@link DeviceList} with status code {@code 200} as a {@link ResponseEntity} 
     */
    public static ResponseEntity<Object> deleteDevices() {
        ListService.getDeviceList().clearList();

        PersistentDataService.deleteDeviceListSave();
        PersistentDataService.saveVariableList();

        return respondWithDevices(HttpStatus.OK);
    }

    /**
     * Construct and return the {@link DeviceDisplayRepresentation} for all devices of {@link DeviceList} and the necessary hypermedia links.
     * 
     * @param status the {@link HttpStatus} of the response
     * 
     * @return the display representations and hypermedia links as a {@link ResponseEntity} with status {@code status}
     */
    private static ResponseEntity<Object> respondWithDevices(HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("devices", ListService.getDeviceList().getDisplayListRepresentation());
        responseBody.put("_links", ListService.getDeviceList().getLinks("self"));

        return new ResponseEntity<>(responseBody, status);
    }

    /**
     * Return an {@link AbstractDevice}.
     * 
     * @param id the {@link AbstractDevice}'s identifier
     * 
     * @return the {@link AbstractDevice} and necessary hypermedia links as the body of a {@link ResponseEntity} and status code {@code OK}
     */
    public static ResponseEntity<Object> getDevice(String id) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        return respondWithDevice(device, HttpStatus.OK);
    }

    /**
     * Update an {@link AbstractDevice}'s {@code name}.
     * 
     * @param uuid the {@link AbstractDevice}'s {@code id}
     * @param patchMappings a JSON Patch Document
     * 
     * @return the {@link AbstractDevice} and necessary hypermedia links as the body of a {@link ResponseEntity} and status code {@code OK} (if successful)
     */
    public static ResponseEntity<Object> patchDevice(UUID uuid, List<Map<String, Object>> patchMappings) {
        AbstractDevice device = ListService.getDeviceList().getThing(uuid.toString());

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, Object> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.REPLACE));
            patch.validateReplace(String.class);

            patches.add(patch);
        }    

        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("/name");
            if (!patch.isValidPath(pathSegments, ThingType.DEVICE)) {
                throw new FooSHJsonPatchIllegalPathException("You can only edit the field 'name'!");
            }

            patchDeviceName(uuid.toString(), (String) patch.getValue());
        }

        PersistentDataService.saveAll();
        
        return respondWithDevice(device, HttpStatus.OK);
    }

    /**
     * Rename an {@link AbstractDevice}.
     * 
     * @param id the {@link AbstractDevice}'s {@code id}
     * @param patchName the name to change the {@link AbstractDevice}'s {@code name} to
     * 
     * @return {@code true} if renaming the {@link AbstractDevice} was succesful. Otherwise return {@code false}.
     */
    private static boolean patchDeviceName(String id, String patchName) {
        UUID uuid = IdService.isUuid(id).get();

        // Does the field contain any letters, i.e., is it not empty?
        if (patchName.trim().isEmpty()) {
            throw new DeviceNameIsEmptyException(uuid, patchName);
        }

        // Is the name provided by the field unique?
        if (ListService.getDeviceList().isValidName(patchName, uuid)) {
            AbstractDevice device = ListService.getDeviceList().getThing(id.toString());
            device.setName(patchName);
            return true;
        } else {
            throw new DeviceNameIsNotUniqueException(uuid, patchName);
        }
    }

    /**
     * Construct and return the {@link DeviceDisplayRepresentation} of an {@link AbstractDevice} and the necessary hypermedia links.
     * 
     * @param device the {@link AbstractDevice} we want to return
     * @param status the {@link HttpStatus} of the response
     * 
     * @return the display representation and hypermedia links as a {@link ResponseEntity} with status {@code status}
     */
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
