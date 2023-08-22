package com.vs.foosh.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.vs.foosh.api.ApplicationConfig;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.services.ListService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PredictionModelControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void setup() {
        ListService.getDeviceList().clearList();
        ListService.getVariableList().clearList();
        ApplicationConfig.tryToLoadSaveFiles();
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

        ApplicationConfig.tryToLoadSaveFiles();
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
    void givenListWithModel_whenGetModelWithId_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.predictionModel").exists())
            .andExpect(jsonPath("$.predictionModel.id").value(model.getId().toString()));
    }

    @Test
    void givenListWithModel_whenGetModelWithId_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenGetModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.predictionModel").exists())
            .andExpect(jsonPath("$.predictionModel.name").value(model.getName()));
    }

    @Test
    void givenListWithModel_whenGetModelWithName_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(get("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenWithoutModel_whenGetModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(get("/api/models/" + UUID.randomUUID()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    ///
    /// POST api/models/{id}
    ///
    
    @Test
    void givenListWithModel_whenPostModelWithId_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPostModelWithId_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenPostModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPostModelWithName_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(post("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenWithoutModel_whenPostModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(post("/api/models/" + UUID.randomUUID()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    ///
    /// PUT api/models/{id}
    ///

    @Test
    void givenListWithModel_whenPutModelWithId_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPutModelWithId_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenPutModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenPutModelWithName_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(put("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithoutModel_whenPutModel_thenGetProblemDetailWithStatus404() throws Exception {
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

        ApplicationConfig.tryToLoadSaveFiles();
    }

    @Test
    void givenListWithoutModel_whenPatchModel_thenGetProblemDetailWithStatus404() throws Exception {
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
    void givenListWithModel_whenDeleteModelWithId_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getId()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenDeleteModelWithId_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getId()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenListWithModel_whenDeleteModelWithName_thenGetProblemDetailWithStatus405() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getName()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenListWithModel_whenDeleteModelWithName_thenGetNonEmptyLinksField() throws Exception {
        ApplicationConfig.tryToLoadSaveFiles();
        AbstractPredictionModel model = ListService.getPredictionModelList().getList().get(0);

        mvc.perform(delete("/api/models/" + model.getName()))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links").isArray())
            .andExpect(jsonPath("$._links").isNotEmpty());
    }

    @Test
    void givenWithoutModel_whenDeleteModel_thenGetProblemDetailWithStatus404() throws Exception {
        mvc.perform(delete("/api/models/" + UUID.randomUUID()))
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
            .andExpect(status().isNotFound());
    }

    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET api/models/{id}/mappings/
    ///

    ///
    /// POST api/models/{id}/mappings/
    ///

    ///
    /// PUT api/models/{id}/mappings/
    ///

    ///
    /// PATCH api/models/{id}/mappings/
    ///

    ///
    /// DELETE api/models/{id}/mappings
    ///
}