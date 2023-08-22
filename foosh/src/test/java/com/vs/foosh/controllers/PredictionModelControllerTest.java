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
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.custom.MyPredictionModel;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PredictionModelControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();
        ListService.getPredictionModelList().addThing(new MyPredictionModel());
    }

    ///
    /// GET api/models/
    ///

    @Test
    void givenAnything_whenGetModels_thenStatus200() throws Exception {
        mvc.perform(get("/api/models/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void givenAnything_whenGetModels_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(get("/api/models/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenNoModels_whenGetModels_thenGetEmptyArray() throws Exception {
        ListService.getPredictionModelList().clearList();
        
        mvc.perform(get("/api/models/"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.predictionModels").isArray())
            .andExpect(jsonPath("$.predictionModels").isEmpty())
            .andExpect(jsonPath("$._links").exists());
    }

    ///
    /// POST api/models/
    ///

    @Test
    void givenAnything_whenPostModels_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(post("/api/models/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPostModels_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(post("/api/models/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// PUT api/models/
    ///

    @Test
    void givenAnything_whenPutModels_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(put("/api/models/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPutModels_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(put("/api/models/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// PATCH api/models/
    ///

    @Test
    void givenAnything_whenPatchModels_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(patch("/api/models/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenPatchModels_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(patch("/api/models/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    ///
    /// DELETE api/models/
    ///

    @Test
    void givenAnything_whenDeleteModels_thenGetProblemDetailWithStatus405() throws Exception {
        mvc.perform(delete("/api/models/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnything_whenDeleteModels_thenGetNonEmptyLinksField() throws Exception {
        mvc.perform(delete("/api/models/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET api/models/{id}
    ///

    @Test
    void givenListWithModel_whenGetModelWithUuid_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.predictionModel").exists())
            .andExpect(jsonPath("$.predictionModel.id").value(model.getId().toString()));
    }

    @Test
    void givenListWithModel_whenGetModelWithUuid_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenGetModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.predictionModel").exists())
            .andExpect(jsonPath("$.predictionModel.name").value(model.getName()));
    }

    @Test
    void givenListWithModel_whenGetModelWithName_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithoutModel_whenGetModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(get("/api/models/" + UUID.randomUUID()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    ///
    /// POST api/models/{id}
    ///
    
    @Test
    void givenListWithModel_whenPostModelWithUuid_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPostModelWithUuid_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenPostModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPostModelWithName_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithoutModel_whenPostModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(post("/api/models/" + UUID.randomUUID()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    ///
    /// PUT api/models/{id}
    ///

    @Test
    void givenListWithModel_whenPutModelWithUuid_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPutModelWithUuid_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenPutModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPutModelWithName_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListListWithoutModel_whenPutModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(put("/api/models/" + UUID.randomUUID()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    ///
    /// PATCH api/models/{id}
    ///

    @Test
    void givenAnything_whenPatchWithNonJSON_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(patch("/api/models/test-model").contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenAnEmptyList_whenPatchModel_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getPredictionModelList().clearList();

        mvc.perform(patch("/api/models/test-model").contentType("application/json-patch+json"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenListListWithoutModel_whenPatchModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(patch("/api/models/" + UUID.randomUUID())
            .contentType("application/json-patch+json")
            .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithModel_whenPatchModelWithName_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/devices/" + model.getName())
            .contentType("application/json-patch+json")
            .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
 
    @Test
    void givenListWithModel_whenPatchModelWithNoContent_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/devices/" + model.getId())
            .contentType("application/json-patch+json"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
 
    @Test
    void givenListWithModel_whenPatchModelWithContent_thenGetDeviceWithStatus200() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/name\",\"value\":\"test-model\"}]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.predictionModel").exists())
                .andExpect(jsonPath("$.predictionModel.id").value(model.getId().toString()))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithModel_whenPatchModelWithWrongFormat_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"path\":\"/name\",\"value\":\"test-model\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelWithIllegalPath_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"" + UUID.randomUUID() + "\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void givenListWithModel_whenPatchModelWithIllegalJsonPatchOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"delete\",\"path\":\"/id\",\"value\":\"test-model\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelWithDisallowedOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/id\",\"value\":\"test-model\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelWithNoValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelWithEmptyValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelWithWrongFormatValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getId())
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"replace\",\"path\":\"/id\",\"value\":\"[]\"}]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    ///
    /// DELETE api/models/{id}
    ///

    @Test
    void givenListWithModel_whenDeleteModelWithUuid_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenDeleteModelWithUuid_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenDeleteModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenDeleteModelWithName_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithoutModel_whenDeleteModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(delete("/api/models/" + UUID.randomUUID()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET api/models/{id}/mappings/
    ///

    @Test
    void givenAnEmptyList_whenGetModelMapping_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getPredictionModelList().clearList();

        mvc.perform(get("/api/models/" + UUID.randomUUID() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenANonEmptyList_whenGetModelMapping_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(get("/api/models/" + UUID.randomUUID() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());

    }

    @Test
    void givenModelWithMapping_whenGetModelMappingWithUuid_thenGetMappingsList() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getId() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mappings").exists())
            .andExpect(jsonPath("$.mappings").isArray())
            .andExpect(status().isOk());
    }

    @Test
    void givenModelWithMapping_whenGetModelMappingWithName_thenGetMappingsList() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getName() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mappings").exists())
            .andExpect(jsonPath("$.mappings").isArray())
            .andExpect(status().isOk());
    }

    @Test
    void givenModelWithMapping_whenSuccessfulGetModelMapping_thenGetLinkList() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getId() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(status().isOk());
    }

    ///
    /// POST api/models/{id}/mappings/
    ///

    @Test
    void givenAnEmptyList_whenPostModelMapping_thenGetProblemDetailWithStatus404() throws Exception {
        ListService.getPredictionModelList().clearList();

        mvc.perform(post("/api/models/" + UUID.randomUUID() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenANonEmptyList_whenPostModelMapping_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(post("/api/models/" + UUID.randomUUID() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenModel_whenPostModelMappingWithEmptyContent_thenGetProbleDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenModel_whenPostModelMappingWithNoVarId_thenGetProbleDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenModel_whenPostModelMappingWithWrongVarId_thenGetProbleDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": [], \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenModel_whenPostModelMappingWithNoMappings_thenGetProbleDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": " + variable.getId() + "}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenModel_whenPostModelMappingWithWrongMappings_thenGetProbleDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": \"" + variable.getId() + "\", \"mappings\": \"mappings\"}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenModel_whenPostModelMappingWithNonExistendVarId_thenGetProbleDetailWithStatus404() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": \"" + UUID.randomUUID() + "\", \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenModel_whenPostModelMapping_thenGetProbleDetailWithStatus409() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": \"" + variable.getId() + "\", \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.predictionModel").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": \"" + variable.getId() + "\", \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isConflict());
    }

    @Test
    void givenModelWithMapping_whenPostModelMappingSuccessful_thenGetModelAndLinkList() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": \"" + variable.getId() + "\", \"mappings\": []}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.predictionModel").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());
    }

    ///
    /// PUT api/models/{id}/mappings/
    ///

    @Test
    void givenModelWithMapping_whenPutModelMapping_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getId() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenModelWithMapping_whenPutModelMappingWithUuid_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getId() + "/mappings/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenModelWithMapping_whenPutModelMappingWithName_thenGetProblemDetailWithStatus405() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getName() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenModelWithMapping_whenPutModelMappingWithName_thenGetNonEmptyLinksField() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getName() + "/mappings/"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithoutModel_whenPutModelMapping_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(put("/api/models/" + UUID.randomUUID() + "/mappings/"))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    ///
    /// PATCH api/models/{id}/mappings/
    ///

    @Test
    void givenNoModel_whenPatchModelMapping_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(patch("/api/models/" + UUID.randomUUID() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[]"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnything_whenPatchModelMappingWithNonJsonPatch_thenGetProblemDetailWithStatus415() throws Exception {
        mvc.perform(patch("/api/models/" + UUID.randomUUID() + "/mappings/")
            .contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNoContent_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json"))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
 
    @Test
    void givenListWithModel_whenPatchModelMappingWithContent_thenGetModelWithStatus200() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        mvc.perform(post("/api/devices/"));
    
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDevice device = ListService.getDeviceList().getList().get(0);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":[]}]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.predictionModel").exists())
                .andExpect(jsonPath("$.predictionModel.id").value(model.getId().toString()))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithWrongFormat_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"path\":\"/" + variable.getId() + "\",\"value\":[]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModels_whenPatchModelMappingWithIllegalPath_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getName() + "\",\"value\":[]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void givenListWithModel_whenPatchModelMappingWithIllegalJsonPatchOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"duplicate\",\"path\":\"/" + variable.getId() + "\",\"value\":[]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithDisallowedOperator_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"test\",\"path\":\"/" + variable.getId() + "\",\"value\":[]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNoValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithEmptyValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":\"\"}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithWrongFormatValue_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":2}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNonExistentVar_thenGetProblemDetailWithStatus404() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":[]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNonExistentDevice_thenGetProblemDetailWithStatus404() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":[{\"parameter\":\"x1\",\"deviceId\":\"" + device.getId() + "\"}]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNoDeviceId_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":[{\"parameter\":\"x1\"}]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNoParameter_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":[{\"deviceId\":\"" + device.getId() + "\"}]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenListWithModel_whenPatchModelMappingWithNonExistentParameter_thenGetProblemDetailWithStatus400() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);
        
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        mvc.perform(post("/api/vars/" + variable.getId() + "/devices/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"deviceIds\":[\"" + device.getId() + "\"]}"));

        mvc.perform(patch("/api/models/" + model.getName() + "/mappings/")
            .contentType("application/json-patch+json")
            .content("[{\"op\": \"add\",\"path\":\"/" + variable.getId() + "\",\"value\":[{\"parameter\":\"y\",\"deviceId\":\"" + device.getId() + "\"}]}]"))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(status().isBadRequest());
    }

    ///
    /// DELETE api/models/{id}/mappings
    ///

    @Test
    void givenListWithoutModel_whenDeleteModelMapping_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(delete("/api/models/" + UUID.randomUUID() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenListWithModel_whenDeleteEmptyModelMapping_thenGetEmptyMappingsListAndLinks() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mappings").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());
    }

    @Test
    void givenListWithModel_whenDeleteModelMapping_thenGetEmptyMappingsListAndLinks() throws Exception {
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        mvc.perform(post("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"variableId\": \"" + variable.getId() + "\", \"mappings\": []}"))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/models/" + model.getId() + "/mappings/")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mappings").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(status().isOk());
    }
}