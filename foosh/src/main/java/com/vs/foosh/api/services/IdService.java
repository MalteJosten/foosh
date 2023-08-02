package com.vs.foosh.api.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;

public class IdService {
    public static Optional<UUID> isUuid(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        } 

        return Optional.of(uuid);
    }

    public static <T extends Thing> boolean isIdentifierInList(String id, List<T> collection) {
        for(T element: collection) {
            if (element.getId().toString().equals(id) || element.getName().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static <T extends Thing> boolean isUuidInList(UUID id, List<T> collection) {
        for(T element: collection) {
            if (element.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
}
