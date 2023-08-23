package com.vs.foosh.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.vs.foosh.AbstractDeviceTest;
import com.vs.foosh.PredictionModelTest;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DeviceControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();
        ListService.getPredictionModelList().addThing(new PredictionModelTest());
    }

    ///
    /// GET api/devices/
    ///

    @Test
    void givenAnything_whenGetDevices_thenStatus200() throws Exception {
        mvc.perform(get("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void givenAnything_whenGetDevices_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(get("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenNoDevices_whenGetDevices_thenGetEmptyDevicesArray() throws Exception {
        mvc.perform(get("/api/devices/"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.devices").isArray())
            .andExpect(jsonPath("$.devices").isEmpty())
            .andExpect(jsonPath("$._links").exists());
    }

    ///
    /// POST api/devices/
    ///

    @Test
    void givenNoDevices_whenPostDevicesWithoutCredentials_thenGetListOfDevices() throws Exception {
        mvc.perform(post("/api/devices/").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.devices").isArray())
            .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void givenDevices_whenPostDevices_thenGetStatus409() throws Exception {
        List<AbstractDevice> deviceList = List.of(new AbstractDeviceTest("test-device"));
        ListService.getDeviceList().setList(deviceList);

        mvc.perform(post("/api/devices/").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    void givenAnySuccessfulRequest_whenPostDevices_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(post("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }
    
    @Test
    void givenAnything_whenPostDevicesWithNonJSON_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(post("/api/devices/").contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isUnsupportedMediaType());
    }

    ///
    /// PUT api/devices/
    ///

    @Test
    void givenAnything_whenPutDevices_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(put("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
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
    /// PATCH api/devices/
    ///

    @Test
    void givenAnything_whenPatchDevices_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(patch("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPatchDevices_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(patch("/api/devices/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }
    
    ///
    /// DELETE api/devices/
    ///

    @Test
    void givenAnything_whenDeleteDevices_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(delete("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }
    
    @Test
    void givenList_whenDeleteDevices_thenGetEmptyList() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test-device"));

        mvc.perform(delete("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.devices").exists())
            .andExpect(jsonPath("$.devices").isArray())
            .andExpect(jsonPath("$.devices").isEmpty());
    }

    @Test
    void givenAnEmptyList_whenDeleteDevices_thenGetEmptyList() throws Exception {
        mvc.perform(delete("/api/devices/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.devices").exists())
            .andExpect(jsonPath("$.devices").isArray())
            .andExpect(jsonPath("$.devices").isEmpty());
    }

    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET api/devices/{id}
    ///

    @Test
    void givenAnEmptyList_whenGetDevice_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(get("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithoutDevice_whenGetDevice_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test1-device"));

        mvc.perform(get("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithDevice_whenGetDeviceWithName_thenGetDevice() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test-device"));

        mvc.perform(get("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.device").exists())
            .andExpect(jsonPath("$.device.name").value("test-device"));
    }

    @Test
    void givenListWithDevice_whenGetDeviceWithUUID_thenGetDevice() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(get("/api/devices/" + testDevice.getId()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.device").exists())
            .andExpect(jsonPath("$.device.id").value(testDevice.getId().toString()));
    }

    ///
    /// POST api/devices/{id}
    ///

    @Test
    void givenAnEmptyList_whenPostDevice_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(post("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithoutDevice_whenPostDevice_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test1-device"));

        mvc.perform(post("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithDevice_whenPostDevice_thenGetProblemDetailWithStatus405() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test-device"));

        mvc.perform(post("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    ///
    /// PUT api/devices/{id}
    ///

    @Test
    void givenAnEmptyList_whenPutDevice_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(put("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithoutDevice_whenPutDevice_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test1-device"));

        mvc.perform(put("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithDevice_whenPutDevice_thenGetProblemDetailWithStatus405() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test-device"));

        mvc.perform(put("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    ///
    /// PATCH api/devices/{id}
    ///

    @Test
    void givenAnything_whenPatchWithNonJsonPatch_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(patch("/api/devices/test-device").contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenAnEmptyList_whenPatchDevice_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(patch("/api/devices/test-device").contentType("application/json-patch+json"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithoutDevice_whenPatchDevice_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test-device"));

        mvc.perform(patch("/api/devices/" + UUID.randomUUID())
            .contentType("application/json-patch+json")
            .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithDevice_whenPatchDeviceWithName_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getName())
            .contentType("application/json-patch+json")
            .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
 
    @Test
    void givenListWithDevice_whenPatchDeviceWithNoContent_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
 
    @Test
    void givenListWithDevice_whenPatchDeviceWithContent_thenGetDeviceWithStatus200() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/name\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.device").exists())
                .andExpect(jsonPath("$.device.id").value(testDevice.getId().toString()))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithDevice_whenPatchDeviceWithWrongFormat_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"path\":\"/name\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithDevice_whenPatchDeviceWithIllegalPath_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"" + UUID.randomUUID() + "\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void givenListWithDevice_whenPatchDeviceWithIllegalJsonPatchOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"delete\",\"path\":\"/id\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithDevice_whenPatchDeviceWithDisallowedOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/id\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithDevice_whenPatchDeviceWithNoValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithDevice_whenPatchDeviceWithEmptyValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithDevice_whenPatchDeviceWithWrongFormatValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractDeviceTest testDevice = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(testDevice);

        mvc.perform(patch("/api/devices/" + testDevice.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"[]\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    ///
    /// DELETE api/devices/{id}
    ///

    @Test
    void givenAnEmptyList_whenDeleteDevice_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(delete("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithoutDevice_whenDeleteDevice_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test1-device"));

        mvc.perform(delete("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithDevice_whenDeleteDevice_thenGetProblemDetailWithStatus405() throws Exception {
        ListService.getDeviceList().addThing(new AbstractDeviceTest("test-device"));

        mvc.perform(delete("/api/devices/test-device"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }
}
