package com.vs.foosh.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vs.foosh.api.exceptions.variable.VariableNameIsNotUniqueException;
import com.vs.foosh.api.exceptions.variable.VariableNameMustNotBeAnUuidException;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.helpers.ListService;

public class VariableListTest {

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
    void givenVarsListWithOneVar_whenIsValidWithRandomString_getTrue() {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        assertEquals(true, ListService.getVariableList().isValidName("test-other", variable.getId()));
    }

    @Test
    void givenVarsListWithOneVar_whenIsValidWithUuid_getException() {
        Variable variable = new Variable("test-var", List.of(), List.of());
        ListService.getVariableList().addThing(variable);

        assertThrows(VariableNameMustNotBeAnUuidException.class, () -> {
            ListService.getVariableList().isValidName(UUID.randomUUID().toString(), variable.getId());
        });
    }

    @Test
    void givenNonEmptyVarsList_whenIsValidWithRandomString_getTrue() {
        Variable variable1 = new Variable("test-var1", List.of(), List.of());
        Variable variable2 = new Variable("test-var2", List.of(), List.of());
        ListService.getVariableList().addThing(variable1);
        ListService.getVariableList().addThing(variable2);

        assertEquals(true, ListService.getVariableList().isValidName("test-other", variable2.getId()));
    }

    @Test
    void givenNonEmptyVarsList_whenIsValidWithUuid_getException() {
        Variable variable1 = new Variable("test-var1", List.of(), List.of());
        Variable variable2 = new Variable("test-var2", List.of(), List.of());
        ListService.getVariableList().addThing(variable1);
        ListService.getVariableList().addThing(variable2);

        assertThrows(VariableNameMustNotBeAnUuidException.class, () -> {
            ListService.getVariableList().isValidName(UUID.randomUUID().toString(), variable2.getId());
        });
    }

    @Test
    void givenNonEmptyVarsList_whenIsValidWithOwnDuplicate_getTrue() {
        Variable variable1 = new Variable("test-var1", List.of(), List.of());
        Variable variable2 = new Variable("test-var2", List.of(), List.of());
        ListService.getVariableList().addThing(variable1);
        ListService.getVariableList().addThing(variable2);

        assertEquals(true, ListService.getVariableList().isValidName("test-var2", variable2.getId()));
    }

    @Test
    void givenNonEmptyVarsList_whenIsValidWithDuplicate_getException() {
        Variable variable1 = new Variable("test-var1", List.of(), List.of());
        Variable variable2 = new Variable("test-var2", List.of(), List.of());
        ListService.getVariableList().addThing(variable1);
        ListService.getVariableList().addThing(variable2);

        assertThrows(VariableNameIsNotUniqueException.class, () -> {
            ListService.getVariableList().isValidName("test-var1", variable2.getId());
        });
    }
}
