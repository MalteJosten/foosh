package com.vs.foosh.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vs.foosh.api.exceptions.misc.HttpMappingNotAllowedException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeDetails;
import com.vs.foosh.api.services.DeviceService;
import com.vs.foosh.api.services.helpers.LinkBuilderService;
import com.vs.foosh.api.services.helpers.ListService;

/**
 * A {@link RestController} that handles HTTP requests for the routes {@code /api/devices/} and {@code /api/devices/{id}}.
 */
@RestController
@RequestMapping(value="/api/devices")
public class DeviceController {
    
    /**
     * The controllers "root" route.  
     */
    private final String ROUTE = "/api/devices/";

    //
    // Device Collection
    //

    /**
     * Handle incoming {@code GET} requests on route {@code /api/devices/} using {@link GetMapping}.
     * 
     * Retrieve the list of {@link AbstractDevice}s.
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesGet() {
        return DeviceService.getDevices();
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/devices/} using {@link PostMapping}.
     * 
     *  Retrieve the list of smart home devices from the smart home API and store it.
     * 
     * @apiNote It only accepts {@code application/json} {@link MediaType}s.
     * 
     * @param credentials the smart home credentials
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesPost(
            @RequestBody(required = false) SmartHomeDetails details) {
        
        return DeviceService.postDevices(details);

    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/devices/} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PutMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void devicesPut() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + "! Use DELETE and POST to replace the list of devices.",
                ListService.getDeviceList().getLinks("self"));
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/devices} using {@link PatchMapping}.
     * 
     * @apiNote Using {@code PATCH} on this route is not allowed. Hence, a {@link HttpMappingNotAllowedException} is thrown.
     */
    @PatchMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void devicesPatch() {
        throw new HttpMappingNotAllowedException(
                "You cannot use PATCH on " + ROUTE + "! Either use PATCH on /devices/{id} to update the device's name or DELETE and POST to replace the list of devices.",
                ListService.getDeviceList().getLinks("self"));
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/devices} using {@link DeleteMapping}.
     * 
     * Delete the list of {@link AbstractDevice}s.
     *
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @DeleteMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicesDelete() {
        return DeviceService.deleteDevices();
    }

    //
    // Device
    //

    /**
     * Handle incoming {@code GET} requests on route {@code /api/devices/{id}} using {@link GetMapping}.
     * 
     * Get a specific {@link AbstractDevice}.
     * 
     * @apiNote {id} in the route can be either the {@link AbstractDevice}'s {@code name} or {@code id}.
     * 
     * @param id the identifier of the the {@link Variable}
     * @return the HTTP repsonse as a {@link ResponseEntity}
     */
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deviceGet(@PathVariable("id") String id) {
        return DeviceService.getDevice(id);
    }

    /**
     * Handle incoming {@code POST} requests on route {@code /api/devices/{id}} using {@link PostMapping}.
     * 
     * @apiNote Using {@code POST} on this route is not allowed. Hence, a {@link HttpmAppingNotAllowedException} is thrown.
     *
     * @param id the identifier of the the {@link Variable}
     */
    @PostMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void devicePost(@PathVariable("id") String id) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.POST, List.of("application/json")));
        links.addAll(device.getSelfLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use POST on " + ROUTE + id.replace(" ", "%20") +  "! Please use POST instead.",
                links);
    }

    /**
     * Handle incoming {@code PUT} requests on route {@code /api/devices/{id}} using {@link PutMapping}.
     * 
     * @apiNote Using {@code PUT} on this route is not allowed. Hence, a {@link HttpmAppingNotAllowedException} is thrown.
     *
     * @param id the identifier of the the {@link Variable}
     */
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void devicePut(@PathVariable("id") String id) {
        AbstractDevice device = ListService.getDeviceList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.POST, List.of("application/json")));
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.DELETE, List.of()));
        links.addAll(device.getSelfLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot use PUT on " + ROUTE + id.replace(" ", "%20") +  "! Either use PATCH on /devices/" + device.getId() + " to edit " + device.getName() + "'s name or use DELETE and POST on /devices/ to update the entire collection of devices.",
                links);
    }

    /**
     * Handle incoming {@code PATCH} requests on route {@code /api/devices/{id}} using {@link PatchMapping}.
     * 
     * Patch (Edit) the name of a specific {@link AbstractDevice}.
     * 
     * @apiNote It only accepts {@code application/json-patch+json} Content-Type.
     * @see <a href="https://www.rfc-editor.org/rfc/rfc6902">RFC 6902: JavaScript Object Notation Patch</a>
     *
     * @param id the identifier of the the {@link Variable}
     * @param patchMappings a Json Patch Document
     * 
     * @return the HTTP response as a {@link ResponseEntity}
     */
    @PatchMapping(value = "/{id}",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> devicePatch(@PathVariable("id") UUID uuid, @RequestBody List<Map<String, Object>> patchMappings) {
        return DeviceService.patchDevice(uuid, patchMappings);
    }

    /**
     * Handle incoming {@code DELETE} requests on route {@code /api/devices/{id}} using {@link DeleteMapping}.
     * 
     * @apiNote Using {@code DELETE} on this route is not allowed. Hence, a {@link HttpmAppingNotAllowedException} is thrown.
     *
     * @param id the identifier of the the {@link Variable}
     */
    @DeleteMapping("/{id}")
    public void deviceDelete(@PathVariable("id") String id) {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink(), HttpAction.DELETE, List.of()));
        links.addAll(ListService.getDeviceList().getThing(id).getSelfLinks());

        throw new HttpMappingNotAllowedException(
                "You cannot delete an individual device. You can only delete the entire collection with DELETE on /devices/ !",
                links);
    }

}
