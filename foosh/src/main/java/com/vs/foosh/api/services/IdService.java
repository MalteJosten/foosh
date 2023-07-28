package com.vs.foosh.api.services;

import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.Thing;

// TODO: Add method to check whether String is an UUID
public class IdService {
    public static boolean isUuid(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return false;
        } 

        return true;
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
