package com.vs.foosh.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vs.foosh.api.exceptions.device.DeviceIdNotFoundException;
import com.vs.foosh.api.exceptions.predictionModel.MalformedParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingDeviceException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNotFoundException;
import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPostRequest;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.helper.AbstractDeviceTest;
import com.vs.foosh.helper.PredictionModelTest;

public class PredictionModelMappingPostRequestTest {

    private UUID modelId;
    private UUID variableId;
    private UUID deviceId;
    
    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();

        ListService.getPredictionModelList().addThing(new PredictionModelTest("test-model"));
        modelId = ListService.getPredictionModelList().getList().get(0).getId();

        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);
        deviceId = device.getId();

        Variable variable = new Variable("test-var",  List.of(), List.of());
        variable.addDevice(device.getId());
        ListService.getVariableList().addThing(variable);
        variableId = variable.getId();
    }

    ///
    /// validate()
    ///

    @Test
    void givenAnything_whenVariableIdIsNotInVarList_getException() {
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(UUID.randomUUID(), List.of());

        assertThrows(VariableNotFoundException.class, () -> {
            request.validate(modelId.toString(), List.of());  
        });
    }

    @Test
    void givenVariableIdIsNull_whenValidate_getException() {
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(null, List.of());

        assertThrows(VariableNotFoundException.class, () -> {
            request.validate(modelId.toString(), List.of());  
        });
    }

    @Test
    void givenModelIdIsNotInModelList_whenValidate_getException() {
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of());

        assertThrows(PredictionModelNotFoundException.class, () -> {
            request.validate(UUID.randomUUID().toString(), List.of());  
        });
    }

    @Test
    void givenMappingHasNoParameter_whenValidate_getException() {
        ParameterMapping mapping = new ParameterMapping(null, deviceId.toString());
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping));

        assertThrows(MalformedParameterMappingException.class, () -> {
            request.validate(modelId.toString(), List.of(deviceId));
        });
    }

    @Test
    void givenMappingHasEmptyParameter_whenValidate_getException() {
        ParameterMapping mapping = new ParameterMapping("", deviceId.toString());
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping));

        assertThrows(MalformedParameterMappingException.class, () -> {
            request.validate(modelId.toString(), List.of(deviceId));
        });
    }

    @Test
    void givenMappingHasDuplicateParameter_whenValidate_getException() {
        ParameterMapping mapping1 = new ParameterMapping("x1", deviceId.toString());
        ParameterMapping mapping2 = new ParameterMapping("x1", deviceId.toString());
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping1, mapping2));

        assertThrows(MalformedParameterMappingException.class, () -> {
            request.validate(modelId.toString(), List.of(deviceId));
        });
    }

    @Test
    void givenMappingHasNoDeviceId_whenValidate_getException() {
        ParameterMapping mapping = new ParameterMapping("x1", null);
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping));

        assertThrows(MalformedParameterMappingException.class, () -> {
            request.validate(modelId.toString(), List.of(deviceId));
        });
    }

    @Test
    void givenMappingHasEmptyDeviceId_whenValidate_getException() {
        ParameterMapping mapping = new ParameterMapping("x1", "");
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping));

        assertThrows(MalformedParameterMappingException.class, () -> {
            request.validate(modelId.toString(), List.of(deviceId));
        });
    }

    @Test
    void givenMappingHasDeviceIdNotInDeviceList_whenValidate_getException() {
        String randomUUID = UUID.randomUUID().toString();
        ParameterMapping mapping = new ParameterMapping("x1", randomUUID);
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping));

        assertThrows(DeviceIdNotFoundException.class, () -> {
            request.validate(modelId.toString(), List.of(UUID.fromString(randomUUID)));
        });
    }

    @Test
    void givenMappingHasDeviceIdNotInVarDeviceList_whenValidate_getException() {
        AbstractDeviceTest device2 = new AbstractDeviceTest("test-device2");
        ListService.getDeviceList().addThing(device2);

        ParameterMapping mapping = new ParameterMapping("x1", device2.getId().toString());
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping));

        assertThrows(ParameterMappingDeviceException.class, () -> {
            request.validate(modelId.toString(), List.of(deviceId));
        });
    }

    @Test
    void givenWellformedObject_whenValidate_getSuccess() {
        ParameterMapping mapping = new ParameterMapping("x1", deviceId.toString());
        PredictionModelMappingPostRequest request = new PredictionModelMappingPostRequest(variableId, List.of(mapping));

        try {
            request.validate(modelId.toString(), List.of(deviceId));
        } catch (Exception e) {
            assertEquals(true, false);
        }
    }

}
