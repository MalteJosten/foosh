package com.vs.foosh.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchFormatException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.FooSHPatchOperation;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

public class FooSHJsonPatchTest {
    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();
    }

    ///
    /// validateRequest()
    ///

    @Test
    void givenNoOperationField_whenValidateRequest_getException() {
        Map<String, Object> request = new HashMap<>();
        FooSHJsonPatch patch = new FooSHJsonPatch(request);
        assertThrows(FooSHJsonPatchFormatException.class, () -> {
            patch.validateRequest(List.of());
        });
    }

    @Test
    void givenEmptyOperationField_whenValidateRequest_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);
        assertThrows(FooSHJsonPatchIllegalOperationException.class, () -> {
            patch.validateRequest(List.of());
        });
    }

    @Test
    void givenNoJsonPatchOperation_whenValidateRequest_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "delete");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);
        assertThrows(FooSHJsonPatchIllegalOperationException.class, () -> {
            patch.validateRequest(List.of());
        });
    }

    @Test
    void givenNotAllowedPatchOperation_whenValidateRequest_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "remove");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        assertThrows(FooSHJsonPatchIllegalOperationException.class, () -> {
            patch.validateRequest(List.of(FooSHPatchOperation.ADD));
        });
    }

    ///
    /// validateAdd()
    ///
    
    @Test
    void givenRequestContainsNotAllFields_whenValidateAdd_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "/");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        assertThrows(FooSHJsonPatchFormatException.class, () -> {
            patch.validateAdd(Object.class);
        });
    }

    @Test
    void givenRequestContainsEmptyPath_whenValidateAdd_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "");
        request.put("value", "123");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        assertThrows(FooSHJsonPatchFormatException.class, () -> {
            patch.validateAdd(Object.class);
        });
    }

    @Test
    void givenRequest_whenValidateAddWithUnknownClass_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "/");
        request.put("value", "abc");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        assertThrows(FooSHJsonPatchValueException.class, () -> {
            patch.validateAdd(Object.class);
        });
    }

    @Test
    void givenRequestStringValueIsString_whenValidateAdd_getSetPathAndValue() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "/");
        request.put("value", "abc");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        patch.validateAdd(String.class);
        
        assertEquals(patch.getPath(), "/");
        assertEquals(patch.getValue(), "abc");
    }

    @Test
    void givenRequestStringValueIsEmptyString_whenValidateAdd_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "/");
        request.put("value", "");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        assertThrows(FooSHJsonPatchValueIsEmptyException.class, () -> {
            patch.validateAdd(String.class);
        });
    }

    @Test
    void givenRequestStringValueIsNoString_whenValidateAdd_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "/");
        request.put("value", List.of());
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        assertThrows(FooSHJsonPatchValueException.class, () -> {
            patch.validateAdd(String.class);
        });
    }

    @Test
    void givenRequestUuidValueIsUuid_whenValidateAdd_getSetPathAndValue() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "/");
        String randUuid = UUID.randomUUID().toString();
        request.put("value", randUuid);
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        patch.validateAdd(UUID.class);
        
        assertEquals(patch.getPath(), "/");
        assertEquals(patch.getValue(), randUuid);
    }

    @Test
    void givenRequestUuidValueIsNoUuid_whenValidateAdd_getException() {
        Map<String, Object> request = new HashMap<>();
        request.put("op", "add");
        request.put("path", "/");
        request.put("value", "abc");
        FooSHJsonPatch patch = new FooSHJsonPatch(request);

        assertThrows(FooSHJsonPatchValueException.class, () -> {
            patch.validateAdd(UUID.class);
        });
    }
}
