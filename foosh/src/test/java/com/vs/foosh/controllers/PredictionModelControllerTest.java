package com.vs.foosh.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PredictionModelControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void setup() {
        ListService.getDeviceList().clearList();
        ListService.getVariableList().clearList();
        PersistentDataService.hasSavedPredictionModelList();
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

        PersistentDataService.hasSavedPredictionModelList();
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

    ///
    /// POST api/models/{id}
    ///

    ///
    /// PUT api/models/{id}
    ///

    ///
    /// PATCH api/models/{id}
    ///

    ///
    /// DELETE api/models/{id}
    ///

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