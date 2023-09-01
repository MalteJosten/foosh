package com.vs.foosh.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNotFoundException;
import com.vs.foosh.api.exceptions.variable.MalformedVariablePredictionRequest;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariablePredictionRequest;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.helpers.ListService;
import com.vs.foosh.helper.PredictionModelMock;

public class VariablePredictionRequestTest {

    private UUID modelId;
    private UUID variableId;
    
    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();

        ListService.getPredictionModelList().addThing(new PredictionModelMock("test-model"));
        modelId = ListService.getPredictionModelList().getList().get(0).getId();

        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);
        variableId = variable.getId();
    }

    ///
    /// validate()
    ///

    // modelId not present


    @Test
    void givenVariableIdIsNotInVarList_whenValidate_getException() {
        VariablePredictionRequest request = new VariablePredictionRequest(modelId.toString(), "1", false);

        assertThrows(VariableNotFoundException.class, () -> {
            request.validate(UUID.randomUUID().toString());  
        });
    }

    @Test
    void givenVariableIdIsNull_whenValidate_getException() {
        VariablePredictionRequest request = new VariablePredictionRequest(modelId.toString(), "1", false);

        assertThrows(VariableNotFoundException.class, () -> {
            request.validate(UUID.randomUUID().toString());  
        });
    }

    @Test
    void givenModelIdIsNull_whenValidate_getException() {
        VariablePredictionRequest request = new VariablePredictionRequest(null, "1", false);

        assertThrows(MalformedVariablePredictionRequest.class, () -> {
            request.validate(variableId.toString());  
        });
    }

    @Test
    void givenModelIdIsEmpty_whenValidate_getException() {
        VariablePredictionRequest request = new VariablePredictionRequest("", "1", false);

        assertThrows(MalformedVariablePredictionRequest.class, () -> {
            request.validate(variableId.toString());  
        });
    }

    @Test
    void givenModelIsNotInModelList_whenValidate_getException() {
        VariablePredictionRequest request = new VariablePredictionRequest(UUID.randomUUID().toString(), "1", false);

        assertThrows(PredictionModelNotFoundException.class, () -> {
            request.validate(variableId.toString());  
        });
    }

    @Test
    void givenValueIsNull_whenValidate_getException() {
        VariablePredictionRequest request = new VariablePredictionRequest(modelId.toString(), null, false);

        assertThrows(MalformedVariablePredictionRequest.class, () -> {
            request.validate(variableId.toString());  
        });
    }

    @Test
    void givenValueIsEmpty_whenValidate_getException() {
        VariablePredictionRequest request = new VariablePredictionRequest(modelId.toString(), "", false);

        assertThrows(MalformedVariablePredictionRequest.class, () -> {
            request.validate(variableId.toString());  
        });
    }
}
