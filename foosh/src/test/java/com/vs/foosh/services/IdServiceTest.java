package com.vs.foosh.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.vs.foosh.api.services.helpers.IdService;
import com.vs.foosh.helper.ThingMock;

public class IdServiceTest {
    @Test
    void givenUuid_whenIsUuid_getOptionalUuid() {
        UUID id = UUID.randomUUID();
        String idString = id.toString();

        assertEquals(true, IdService.isUuid(idString).isPresent());
        assertEquals(id, IdService.isUuid(idString).get());
    }

    @Test
    void givenNoUuid_whenIsUuid_getOptionalEmpty() {
        assertEquals(true, IdService.isUuid("abc").isEmpty());
    }

    @Test
    void givenAnythingAndNullList_whenIsIdentifierInList_getFalse() {
        UUID id = UUID.randomUUID();
        String idString = id.toString();

        assertEquals(false, IdService.isIdentifierInList(idString, null));
        assertEquals(false, IdService.isIdentifierInList("abc", null));
    }

    @Test
    void givenNullAndList_whenIsIdentifierInList_getFalse() {
        assertEquals(false, IdService.isIdentifierInList(null, List.of()));
    }

    @Test
    void givenUuidAndEmptyList_whenIsIdentifierInList_getFalse() {
        assertEquals(false, IdService.isIdentifierInList(UUID.randomUUID().toString(), List.of()));
    }

    @Test
    void givenStringAndEmptyList_whenIsIdentifierInList_getFalse() {
        assertEquals(false, IdService.isIdentifierInList("abc", List.of()));
    }

    @Test
    void givenUuidAndList_whenIsIdentifierInListWithUuid_getFalse() {
        ThingMock thing = new ThingMock("test-thing");
        assertEquals(true, IdService.isIdentifierInList(thing.getId().toString(), List.of(thing)));
    }

    @Test
    void givenUuidAndList_whenIsIdentifierInListWithoutUuid_getFalse() {
        assertEquals(false, IdService.isIdentifierInList(UUID.randomUUID().toString(), List.of(new ThingMock("test-thing"))));
    }

    @Test
    void givenStringAndList_whenIsIdentifierInListWithString_getFalse() {
        ThingMock thing = new ThingMock("test-thing");
        assertEquals(true, IdService.isIdentifierInList("test-thing", List.of(thing)));
    }

    @Test
    void givenStringAndList_whenIsIdentifierInListWithoutString_getFalse() {
        assertEquals(false, IdService.isIdentifierInList("abc", List.of(new ThingMock("test-thing"))));
    }
}
