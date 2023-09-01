package com.vs.foosh.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.helpers.ListService;
import com.vs.foosh.helper.AbstractDeviceMock;
import com.vs.foosh.helper.PredictionModelMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class VariableControllerTest {

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();
        ListService.getPredictionModelList().addThing(new PredictionModelMock("test-model"));
    }

    ///
    /// GET /api/vars/
    ///

    @Test
    void givenAnything_whenGetVars_thenStatus200() throws Exception {
        mvc.perform(get("/api/vars/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAnything_whenGetVars_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(get("/api/vars/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenNoVariables_whenGetVars_thenGetEmptyVarArray() throws Exception {
        mvc.perform(get("/api/vars/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$.variables").isEmpty())
                .andExpect(jsonPath("$._links").exists());
    }

    ///
    /// POST /api/vars/
    ///

    @Test
    void givenNoVariables_whenPostVar_thenGetListOfVars() throws Exception {
        mvc.perform(post("/api/vars/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"test\"}")
                .header("batch", false))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$._links").exists());

        ListService.getVariableList().clearList();

        mvc.perform(post("/api/vars/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("[{\"name\": \"test\"}]")
                .header("batch", true))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void givenAnySuccessfulRequest_whenPostVar_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(post("/api/vars/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"test\"}")
                .header("batch", false))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());

        ListService.getVariableList().clearList();

        mvc.perform(post("/api/vars/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("[{\"name\": \"test\"}]")
                .header("batch", true))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenNoVariables_whenPostVarWithWrongContent_thenGetProblemDetailWithStatus400() throws Exception {
        mvc.perform(post("/api/vars/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}")
                .header("batch", false))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());

        ListService.getVariableList().clearList();

        mvc.perform(post("/api/vars/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("[]")
                .header("batch", true))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnything_whenPostVarsWithNonJSON_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(post("/api/vars/").contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isUnsupportedMediaType());
    }

    ///
    /// PUT /api/vars/
    ///

    @Test
    void givenAnything_whenPutVars_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(put("/api/vars/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPutVars_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(put("/api/vars/"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// PATCH /api/vars/
    ///

    @Test
    void givenAnything_whenPatchVars_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(patch("/api/vars/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPatchVars_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(patch("/api/vars/"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// DELETE /api/vars/
    ///

    @Test
    void givenAnything_whenDeleteVars_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(delete("/api/vars/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenList_whenDeleteVars_thenGetEmptyList() throws Exception {
        ListService.getVariableList().addThing(new Variable("test-var", List.of(), List.of()));

        mvc.perform(delete("/api/vars/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variables").exists())
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$.variables").isEmpty());
    }

    @Test
    void givenAnEmptyList_whenDeleteVars_thenGetEmptyList() throws Exception {
        mvc.perform(delete("/api/vars/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variables").exists())
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$.variables").isEmpty());
    }

    /// ----------------------------------------------------------------------------------
    /// ///

    ///
    /// GET /api/vars/{id}
    ///

    @Test
    void givenAnEmptyList_whenGetVar_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(get("/api/vars/test-var"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAListWithVariable_whenGetVarWithUuid_thenGetVariable() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getId()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variable").exists())
                .andExpect(jsonPath("$.variable.id").value(variable.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void givenAListWithVariable_whenGetVarWithName_thenGetVariable() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getName()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variable").exists())
                .andExpect(jsonPath("$.variable.name").value(variable.getName()))
                .andExpect(status().isOk());
    }

    @Test
    void givenAListWithoutVar_whenGetVarWithName_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getVariableList().addThing(new Variable("test-var", List.of(), List.of()));

        mvc.perform(get("/api/vars/var-name"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    ///
    /// POST /api/vars/{id}
    ///
    
    @Test
    void givenAListWithoutVar_whenPostVar_thenGenProblemDetailWithStatus404() throws Exception {
        mvc.perform(post("/api/vars/test-var")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithVar_whenPostModelMappingWithEmptyContent_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPostVarWithNoModelId_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"value\": 0, \"execute\": true}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPostVarWithWrongModelId_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"modelId\": [], \"value\": 0, \"execute\": true}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPostVarWithNoModels_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"modelId\": \"" + model.getId() + "\", \"value\": 0, \"execute\": true}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPostVarWithNonExistendModelId_thenGetProblemDetailWithStatus404() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"modelId\": \"" + UUID.randomUUID() + "\", \"value\": 0, \"execute\": true}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithVar_whenPostVarWithoutExecuteSuccessful_thenGetInstructions() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/devices/"))
            .andExpect(status().isCreated());

        AbstractDevice device = ListService.getDeviceList().getList().get(0);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device.getId() + "\"]}"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.devices").exists())
                    .andExpect(jsonPath("$.devices").isArray())
                    .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": [{\"parameter\":\"x1\", \"deviceId\":\"" + device.getId() + "\"}]}"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.models").exists())
                    .andExpect(jsonPath("$.models").isArray())
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$._links").isArray())
                    .andExpect(jsonPath("$._links").isNotEmpty())
                    .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"modelId\": \"" + model.getId() + "\", \"value\": 0}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variable").exists())
                .andExpect(jsonPath("$.value").exists())
                .andExpect(jsonPath("$.instructions").exists())
                .andExpect(jsonPath("$.instructions").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithVar_whenPostVarWithTrueExecuteSuccessful_thenGetInstructions() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/devices/"))
            .andExpect(status().isCreated());

        AbstractDevice device = ListService.getDeviceList().getList().get(0);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device.getId() + "\"]}"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.devices").exists())
                    .andExpect(jsonPath("$.devices").isArray())
                    .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": [{\"parameter\":\"x1\", \"deviceId\":\"" + device.getId() + "\"}]}"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.models").exists())
                    .andExpect(jsonPath("$.models").isArray())
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$._links").isArray())
                    .andExpect(jsonPath("$._links").isNotEmpty())
                    .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"modelId\": \"" + model.getId() + "\", \"value\": 0, \"execute\": true}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variable").exists())
                .andExpect(jsonPath("$.value").exists())
                .andExpect(jsonPath("$.instructions").exists())
                .andExpect(jsonPath("$.instructions").isArray())
                .andExpect(jsonPath("$.responses").exists())
                .andExpect(jsonPath("$.responses").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithVar_whenPostVarWithFalseExecuteSuccessful_thenGetInstructions() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/devices/"))
            .andExpect(status().isCreated());

        AbstractDevice device = ListService.getDeviceList().getList().get(0);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device.getId() + "\"]}"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.devices").exists())
                    .andExpect(jsonPath("$.devices").isArray())
                    .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": [{\"parameter\":\"x1\", \"deviceId\":\"" + device.getId() + "\"}]}"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.models").exists())
                    .andExpect(jsonPath("$.models").isArray())
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$._links").isArray())
                    .andExpect(jsonPath("$._links").isNotEmpty())
                    .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"modelId\": \"" + model.getId() + "\", \"value\": 0, \"execute\": false}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variable").exists())
                .andExpect(jsonPath("$.value").exists())
                .andExpect(jsonPath("$.instructions").exists())
                .andExpect(jsonPath("$.instructions").isArray())
                .andExpect(status().isOk());
    }

    ///
    /// PUT /api/vars/{id}
    ///

    @Test
    void givenAnything_whenPutVar_thenGetProblemDetailWithStatus405() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(put("/api/vars/" + variable.getId()))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPutVar_thenGetNonEmptyLinksField() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        variable.updateLinks();
        ListService.getVariableList().addThing(variable);

        mvc.perform(put("/api/vars/" + variable.getId()))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// PATCH /api/vars/{id}
    ///

    @Test
    void givenAnything_whenPatchVarWithNonJsonPatch_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(patch("/api/vars/test-variable").contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenAnEmptyList_whenPatchVar_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(patch("/api/vars/test-vars").contentType("application/json-patch+json"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithoutVar_whenPatchVariable_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getVariableList().addThing(new Variable("test-var", List.of(), List.of()));

        mvc.perform(patch("/api/vars/" + UUID.randomUUID())
                .contentType("application/json-patch+json")
                .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithVar_whenPatchVarWithName_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getName())
                .contentType("application/json-patch+json")
                .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithNoContent_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithContent_thenGetDeviceWithStatus200() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"op\": \"replace\",\"path\":\"/name\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variable").exists())
                .andExpect(jsonPath("$.variable.id").value(variable.getId().toString()))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithVar_whenPatchVarWithWrongFormat_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"path\":\"/name\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithIllegalPath_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"" + UUID.randomUUID() + "\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithIllegalJsonPatchOperator_thenGetProblemDetailWithStatus400()
            throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"op\": \"delete\",\"path\":\"/id\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithDisallowedOperator_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"op\": \"add\",\"path\":\"/id\",\"value\":\"test-other\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithNoValue_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"op\": \"replace\",\"path\":\"/id\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithEmptyValue_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarWithWrongFormatValue_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId())
                .contentType("application/json-patch+json")
                .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"[]\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    ///
    /// DELETE /api/vars/{id}
    ///

    @Test
    void givenListWithVar_whenDeleteVar_thenGetListWithoutVar() throws Exception {
        mvc.perform(post("/api/vars/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"test-var\"}")
                .header("batch", false))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variables").exists())
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$.variables").isNotEmpty())
                .andExpect(status().isCreated());

        assertEquals(ListService.getVariableList().getList().size(), 1);
        Variable variable = ListService.getVariableList().getList().get(0);

        mvc.perform(delete("/api/vars/" + variable.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.variables").exists())
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$.variables").isEmpty())
                .andExpect(status().isOk());

        assertEquals(ListService.getVariableList().getList().size(), 0);
    }

    @Test
    void givenListWithoutVar_whenDeleteVar_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getVariableList().addThing(new Variable("test-var", List.of(), List.of()));

        mvc.perform(delete("/api/vars/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenEmptyList_whenDeleteVar_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(delete("/api/vars/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    /// ----------------------------------------------------------------------------------
    /// ///

    ///
    /// GET /api/vars/{id}/devices/
    ///

    @Test
    void givenAnEmptyList_whenGetVarDevices_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(get("/api/vars/" + UUID.randomUUID() + "/devices/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenANonEmptyList_whenGetVarDevices_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getVariableList().addThing(new Variable("test-var", List.of(), List.of()));

        mvc.perform(get("/api/vars/" + UUID.randomUUID() + "/devices/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());

    }

    @Test
    void givenVar_whenGetVarDevicesWithUuid_thenGetDevicesList() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getId() + "/devices/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void givenVar_whenGetVarDevicesWithName_thenGetDevicesList() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getId() + "/devices/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void givenVar_whenGetVarDevices_thenGetLinkList() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getId() + "/devices/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(status().isOk());
    }

    ///
    /// POST /api/vars/{id}/devices/
    ///

    @Test
    void givenEmptyList_whenPostVarDevices_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(post("/api/vars/" + UUID.randomUUID() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenVar_whenPostVarDevicesWithNoJson_thenGetProblemDetailWithStatus415() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenVar_whenPostVarDevicesEmptyContent_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarDevicesNoDeviceIds_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarDevicesMalformedDeviceIds_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": 2}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarDevicesNoUuidDeviceIds_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [1]}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarDevicesWithNonExistendDeviceIds_thenGetProblemDetailWithStatus404() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + UUID.randomUUID() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenVar_whenPostVarDevicesSuccessful_thenGetMappings() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device.getId() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void givenVar_whenPostVarDevicesSecondTime_thenGetProblemDetailWithStatus405() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device1 = new AbstractDeviceMock("test-device1");
        ListService.getDeviceList().addThing(device1);
        AbstractDeviceMock device2 = new AbstractDeviceMock("test-device2");
        ListService.getDeviceList().addThing(device2);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device1.getId() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device2.getId() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isMethodNotAllowed());
    }

    ///
    /// PUT /api/vars/{id}/devices/
    ///

    @Test
    void givenAnything_whenPutVarDevices_thenGetProblemDetailWithStatus405() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(put("/api/vars/" + variable.getId() + "/devices/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPutVarDevices_thenGetNonEmptyLinksField() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        variable.updateLinks();
        ListService.getVariableList().addThing(variable);

        mvc.perform(put("/api/vars/" + variable.getId() + "/devices/"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// PATCH /api/vars/{id}/devices/
    ///

    @Test
    void givenNoVar_whenPatchVarDevices_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(patch("/api/vars/" + UUID.randomUUID() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnything_whenPatchVarDevicesWithNonJsonPatch_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(patch("/api/vars/" + UUID.randomUUID() + "/devices/")
            .contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithNoContent_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
 
    @Test
    void givenListWithVar_whenPatchVarDevicesWithContent_thenGetMappingWithStatus200() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\": \"/\",\"value\":\"" + device.getId() + "\"}]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithWrongFormat_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"path\": \"/\", \"value\":\"" + device.getId() + "\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithIllegalPath_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"path\": \"/devices\", \"value\":\"" + device.getId() + "\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void givenListWithVar_whenPatchVarDevicesWithIllegalJsonPatchOperator_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"do-stuff\",\"path\": \"/\",\"value\":\"" + device.getId() + "\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithDisallowedOperator_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"move\",\"path\": \"/\",\"value\":\"" + device.getId() + "\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithNoValue_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"move\",\"path\": \"/\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithEmptyValue_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"move\",\"path\": \"/\",\"value\":\"\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithWrongFormatValue_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"move\",\"path\": \"/\",\"value\":[]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarDevicesWithNonExistentDevice_thenGetProblemDetailWithStatus404() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/devices/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\": \"/\",\"value\":\"" + UUID.randomUUID() + "\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    ///
    /// DELETE /api/vars/{id}/devices/
    ///

    @Test
    void givenListWithoutVar_whenDeleteVarDevices_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(delete("/api/vars/" + UUID.randomUUID() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithVar_whenDeleteVarEmptyDevices_thenGetEmptyDevicesListAndLinks() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        variable.updateLinks();
        ListService.getVariableList().addThing(variable);

        mvc.perform(delete("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(jsonPath("$.devices").isEmpty())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithModel_whenDeleteModelMapping_thenGetEmptyMappingsListAndLinks() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device1 = new AbstractDeviceMock("test-device1");
        ListService.getDeviceList().addThing(device1);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device1.getId() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());

        mvc.perform(delete("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(jsonPath("$.devices").isEmpty())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());
    }

    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET /api/vars/{id}/models/
    ///

    @Test
    void givenAnEmptyList_whenGetVarModels_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(get("/api/vars/" + UUID.randomUUID() + "/models/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenANonEmptyList_whenGetVarModels_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getVariableList().addThing(new Variable("test-var", List.of(), List.of()));

        mvc.perform(get("/api/vars/" + UUID.randomUUID() + "/models/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());

    }

    @Test
    void givenVar_whenGetVarModelsWithUuid_thenGetModelsList() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getId() + "/models/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.models").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void givenVar_whenGetVarModelsWithName_thenGetModelsList() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getId() + "/models/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.models").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void givenVar_whenGetVars_thenGetLinkList() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(get("/api/vars/" + variable.getId() + "/models/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(status().isOk());
    }

    ///
    /// POST /api/vars/{id}/models/
    ///

    @Test
    void givenEmptyList_whenPostVarModels_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(post("/api/vars/" + UUID.randomUUID() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenVar_whenPostVarModelsWithNoJson_thenGetProblemDetailWithStatus415() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenVar_whenPostVarModelsEmptyContent_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarModelsNoModelId_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarModelsNoUuidModelId_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": 2, \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarModelsNonExistendModelId_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + UUID.randomUUID() + "\" , \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenVar_whenPostVarModelsNoMappings_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\"}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarModelsEmptyMappings_thenGetModelListWithLinks() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.models").isArray())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenVar_whenPostVarModelsWrongFormatMappings_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": 2}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenVar_whenPostVarModelsSuccessful_thenGetModelListWithLinks() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device.getId() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": [{\"parameter\":\"x1\", \"deviceId\":\"" + device.getId() + "\"}]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.models").isArray())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenVar_whenPostVarSecondTimes_thenGetProblemDetailWithStatus409() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device1 = new AbstractDeviceMock("test-device1");
        ListService.getDeviceList().addThing(device1);
        AbstractDeviceMock device2 = new AbstractDeviceMock("test-device2");
        ListService.getDeviceList().addThing(device2);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device1.getId() + "\", \"" + device2.getId() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": [{\"parameter\":\"x1\", \"deviceId\":\"" + device1.getId() + "\"}]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.models").isArray())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());

        mvc.perform(post("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelId\": \"" + ListService.getPredictionModelList().getList().get(0).getId() + "\", \"mappings\": [{\"parameter\":\"x2\", \"deviceId\":\"" + device2.getId() + "\"}]}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isConflict());
    }

    ///
    /// PUT /api/vars/{id}/models/
    ///

    @Test
    void givenAnything_whenPutVarModels_thenGetProblemDetailWithStatus405() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(put("/api/vars/" + variable.getId() + "/models/"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPutVarModels_thenGetNonEmptyLinksField() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        variable.updateLinks();
        ListService.getVariableList().addThing(variable);

        mvc.perform(put("/api/vars/" + variable.getId() + "/models/"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// PATCH /api/vars/{id}/models/
    ///

    @Test
    void givenNoVar_whenPatchVarModels_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(patch("/api/vars/" + UUID.randomUUID() + "/models/")
            .contentType("application/json-patch+json")
            .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnything_whenPatchVarModelsWithNonJsonPatch_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(patch("/api/vars/" + UUID.randomUUID() + "/models/")
            .contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithNoContent_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
 
    @Test
    void givenListWithVar_whenPatchVarModelsWithContent_thenGetMappingWithStatus200() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"parameter\":\"x1\",\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.models").isArray())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithWrongFormat_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"parameter\":\"x1\",\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithIllegalPath_thenGetProblemDetailWithStatus400() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/\",\"value\":{\"mappings\":[{\"parameter\":\"x1\",\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void givenListWithVar_whenPatchVarModelsWithIllegalJsonPatchOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"do-stuff\",\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"parameter\":\"x1\",\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithDisallowedOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"copy\",\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"parameter\":\"x1\",\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithNoValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithEmptyValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\",\"value\":\"\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithWrongFormatValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\",\"value\":[]}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithNonExistentModel_thenGetProblemDetailWithStatus404() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + UUID.randomUUID() + "\",\"value\":{\"mappings\":[{\"parameter\":\"x1\",\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNonExistentDevice_thenGetProblemDetailWithStatus404() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"parameter\":\"x1\",\"deviceId\":\"" + UUID.randomUUID() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithVar_whenPatchVarModelWithNoDeviceId_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"parameter\":\"x1\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithVar_whenPatchVarModelsWithNoParameter_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNonExistentParameter_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/vars/" + variable.getId() + "/models/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + model.getId() + "\",\"value\":{\"mappings\":[{\"parameter\":\"y\",\"deviceId\":\"" + device.getId() + "\"}]}}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    ///
    /// DELETE /api/vars/{id}/models/
    ///

    @Test
    void givenListWithoutVar_whenDeleteVarModels_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(delete("/api/vars/" + UUID.randomUUID() + "/models/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithVar_whenDeleteVarEmptyModels_thenGetEmptyDevicesListAndLinks() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        variable.updateLinks();
        ListService.getVariableList().addThing(variable);

        mvc.perform(delete("/api/vars/" + variable.getId() + "/models/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.models").exists())
                .andExpect(jsonPath("$.models").isArray())
                .andExpect(jsonPath("$.models").isEmpty())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithModel_whenDeleteVarModels_thenGetEmptyMappingsListAndLinks() throws Exception {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceIds\": [\"" + device.getId() + "\"]}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(status().isOk());

        mvc.perform(delete("/api/vars/" + variable.getId() + "/devices/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.devices").exists())
                .andExpect(jsonPath("$.devices").isArray())
                .andExpect(jsonPath("$.devices").isEmpty())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());
    }
}
