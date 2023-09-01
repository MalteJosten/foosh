package com.vs.foosh.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNameMustNotBeAnUuidException;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.helpers.ListService;
import com.vs.foosh.helper.PredictionModelMock;

public class PredictionModelListTest {
    
    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();
    }

    ///
    /// isValidName()
    ///

    @Test
    void givenModelsListWithOneModel_whenIsValidWithRandomString_getTrue() {
        PredictionModelMock model = new PredictionModelMock("test-model");
        ListService.getPredictionModelList().addThing(model);

        assertEquals(true, ListService.getPredictionModelList().isValidName("test-other", model.getId()));
    }

    @Test
    void givenModelsListWithOneModel_whenIsValidWithUuid_getException() {
        PredictionModelMock model = new PredictionModelMock("test-model");
        ListService.getPredictionModelList().addThing(model);

        assertThrows(PredictionModelNameMustNotBeAnUuidException.class, () -> {
            ListService.getPredictionModelList().isValidName(UUID.randomUUID().toString(), model.getId());
        });
    }

    @Test
    void givenNonEmptyModelsList_whenIsValidWithRandomString_getTrue() {
        PredictionModelMock model1 = new PredictionModelMock("test-model1");
        PredictionModelMock model2 = new PredictionModelMock("test-model2");
        ListService.getPredictionModelList().addThing(model1);
        ListService.getPredictionModelList().addThing(model2);

        assertEquals(true, ListService.getPredictionModelList().isValidName("test-other", model2.getId()));
    }

    @Test
    void givenNonEmptyModelsList_whenIsValidWithUuid_getException() {
        PredictionModelMock model1 = new PredictionModelMock("test-model1");
        PredictionModelMock model2 = new PredictionModelMock("test-model2");
        ListService.getPredictionModelList().addThing(model1);
        ListService.getPredictionModelList().addThing(model2);

        assertThrows(PredictionModelNameMustNotBeAnUuidException.class, () -> {
            ListService.getPredictionModelList().isValidName(UUID.randomUUID().toString(), model2.getId());
        });
    }

    @Test
    void givenNonEmptyDevicesList_whenIsValidWithOwnDuplicate_getTrue() {
        PredictionModelMock model1 = new PredictionModelMock("test-model1");
        PredictionModelMock model2 = new PredictionModelMock("test-model2");
        ListService.getPredictionModelList().addThing(model1);
        ListService.getPredictionModelList().addThing(model2);

        assertEquals(true, ListService.getPredictionModelList().isValidName("test-model2", model2.getId()));
    }
}
