package com.vs.foosh.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.vs.foosh.AbstractDeviceTest;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.services.ListService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DeviceControllerTest {
    
    @Autowired
    private MockMvc mvc;

    ///
    /// GET /devices/
    ///

    @Test
    void givenAnything_whenGetDevices_thenStatus200() throws Exception {
        mvc.perform(get("/api/devices/"))
            .andExpect(status().isOk());
    }

    @Test
    void givenAnything_whenGetDevices_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(get("/api/devices/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenNoDevices_whenGetDevices_thenGetEmptyDevicesArray() throws Exception {
        ListService.getDeviceList().clearList();
        
        mvc.perform(get("/api/devices/"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.devices").isArray())
            .andExpect(jsonPath("$.devices").isEmpty())
            .andExpect(jsonPath("$._links").exists());
    }

    ///
    /// POST /devices/
    ///

    @Test
    void givenNoDevices_whenPostDevicesWithoutCredentials_thenGetListOfDevices() throws Exception {
        ListService.getDeviceList().clearList();

        mvc.perform(post("/api/devices/").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.devices").isArray())
            .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void givenAnything_whenPostDevices_thenGetStatus409() throws Exception {
        ListService.getDeviceList().clearList();
        List<AbstractDevice> deviceList = List.of(new AbstractDeviceTest("device"));
        // todo: create test device/device description/smarthomeservice/predictionmodel
        ListService.getDeviceList().setList(deviceList);

        mvc.perform(post("/api/devices/").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isConflict());
    }

    @Test
    void givenAnything_whenPostDevices_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(post("/api/devices/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }
    
    @Test
    void givenAnything_whenPostDevicesWithNonJSON_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(post("/api/devices/").contentType(MediaType.TEXT_PLAIN))
            .andExpect(jsonPath("$.type").exists())
            .andExpect(jsonPath("$.title").exists())
            .andExpect(jsonPath("$.status").exists())
            .andExpect(jsonPath("$.status").value(415))
            .andExpect(jsonPath("$.detail").exists())
            .andExpect(jsonPath("$.instance").exists());
    }

    ///
    /// PUT /devices/
    ///

    @Test
    void givenAnything_whenPutDevices_thenGetStatus405() throws Exception {
        mvc.perform(put("/api/devices/"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPutDevices_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(put("/api/devices/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }
    
    ///
    /// DELETE /devices/
    ///



}
