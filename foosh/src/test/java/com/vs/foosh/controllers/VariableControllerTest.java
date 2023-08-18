package com.vs.foosh.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.vs.foosh.VariableTest;
import com.vs.foosh.api.services.ListService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class VariableControllerTest {
    
    @Autowired
    private MockMvc mvc;

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
    void givenNoDevices_whenGetVars_thenGetEmptyVarArray() throws Exception {
        ListService.getVariableList().clearList();
        
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
        ListService.getVariableList().clearList();

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
        ListService.getVariableList().clearList();

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
        ListService.getVariableList().clearList();

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
        ListService.getVariableList().addThing(new VariableTest("test-var"));

        mvc.perform(delete("/api/vars/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.variables").exists())
            .andExpect(jsonPath("$.variables").isArray())
            .andExpect(jsonPath("$.variables").isEmpty());
    }

    @Test
    void givenAnEmptyList_whenDeleteVars_thenGetEmptyList() throws Exception {
        ListService.getVariableList().clearList();

        mvc.perform(delete("/api/vars/"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.variables").exists())
            .andExpect(jsonPath("$.variables").isArray())
            .andExpect(jsonPath("$.variables").isEmpty());
    }


    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET /api/vars/{id}
    ///

    ///
    /// POST /api/vars/{id}
    ///

    ///
    /// PUT /api/vars/{id}
    ///

    ///
    /// PATCH /api/vars/{id}
    ///

    ///
    /// DELETE /api/vars/{id}
    ///

    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET /api/vars/{id}/devices/
    ///

    ///
    /// POST /api/vars/{id}/devices/
    ///

    ///
    /// PUT /api/vars/{id}/devices/
    ///

    ///
    /// PATCH /api/vars/{id}/devices/
    ///

    ///
    /// DELETE /api/vars/{id}/devices/
    ///

    /// ---------------------------------------------------------------------------------- ///

    ///
    /// GET /api/vars/{id}/models/
    ///

    ///
    /// POST /api/vars/{id}/models/
    ///

    ///
    /// PUT /api/vars/{id}/models/
    ///

    ///
    /// PATCH /api/vars/{id}/models/
    ///

    ///
    /// DELETE /api/vars/{id}/models/
    ///

}
