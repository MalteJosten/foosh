package com.vs.foosh.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeCredentials;
import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.services.DeviceService;
import com.vs.foosh.api.services.LinkBuilderService;
import com.vs.foosh.api.services.ListService;

@RestController
@RequestMapping(value="/api/devices")
public class DeviceController {

    //
    // Device Collection
    //

    /**
     * Handle incoming {@code GET} requests on route {@code /api/devices/} using {@link @GetMapping}.
     * 
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesGet() {
        return DeviceService.getDevices();
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/devices/} using {@link @PostMapping}.
     * 
     * @apiNote It only accepts {@code application/json} {@link MediaType}s
     * @param credentials the smart home credentials
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPost(
            @RequestBody(required = false) SmartHomeCredentials credentials) {
        
        return DeviceService.postDevices(credentials);

    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/devices/} using {@link @PutMapping}
     * 
     * @apiNote Using {@code PUT} on this route is prohibited.
     * @param credentials the smart home credentials
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPut() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/! Use DELETE and POST to replace the list of devices.",
                ListService.getDeviceList().getLinks("self"));
    }

    @PatchMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPatch() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on /devices/! Either use PATCH on /devices/{id} to update the device's name or DELETE and POST to replace the list of devices.",
                ListService.getDeviceList().getLinks("self"));
    }

    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesDelete() {
        return DeviceService.deleteDevices();
    }

    //
    // Device
    //

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deviceGet(@PathVariable("id") String id) {
        return DeviceService.getDevice(id);
    }

    @PostMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePost(@PathVariable("id") String id) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.POST, List.of("application/json")));
        links.addAll(device.getSelfLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use POST on /devices/" + id.replace(" ", "%20") +  "! Please use POST instead.",
                links);
    }

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePut(@PathVariable("id") String id) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.POST, List.of("application/json")));
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.DELETE, List.of()));
        links.addAll(device.getSelfLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on /devices/" + id.replace(" ", "%20") +  "! Either use PATCH on /devices/" + device.getId() + " to edit " + device.getName() + "'s name or use DELETE and POST on /devices/ to update the entire collection of devices.",
                links);
    }

    @PatchMapping(value = "/{id}",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePatch(@PathVariable("id") UUID uuid, @RequestBody List<Map<String, Object>> patchMappings) {
        return DeviceService.patchDevice(uuid, patchMappings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deviceDelete(@PathVariable("id") String id) {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.DELETE, List.of()));
        links.addAll(ListService.getDeviceList().getThing(id).getSelfLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot delete an individual device. You can only delete the entire collection with DELETE on /devices/ !",
                links);
    }

}
