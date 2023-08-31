package com.vs.foosh.api.services.helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vs.foosh.api.model.misc.Thing;

/**
 * A {@link Service} that provides some simple functions to work with IDs and lists.
 */
@Service
public class IdService {
    /**
     * Checks whether a provided {@link String} could be a valid {@link UUID}.
     * 
     * @param id the {@link String} under test
     * @return Optional.empyt(), if {@code id} could not be an {@link UUID}
     * @return Optional.of(UUID), if {@code id} could be an {@link UUID}
     */
    public static Optional<UUID> isUuid(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        } 

        return Optional.of(uuid);
    }

    /**
     * Check whether a given identifier is an identifier of a {@link Thing} (either a its {@code id} or {@code name}) in a {@link List} of {@link Thing}s.
     * 
     * @param id the {@link String} identifier to check the existence of
     * @param collection the {@link List} of {@link Thing}s to search for the {@code id}
     * 
     * @return {@code true} if there is a {@link Thing} in {@code collection} with the identifier {@code id}
     */
    public static <T extends Thing> boolean isIdentifierInList(String id, List<T> collection) {
        if (collection == null) {
            return false;
        }

        for(T element: collection) {
            if (element.getId().toString().equals(id) || element.getName().equals(id)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether a given {@link UUID} is an identifier of a {@link Thing} (its {@code id}) in a {@link List} of {@link Thing}s.
     * 
     * @param id the {@link UUID} identifier to check the existence of
     * @param collection the {@link List} of {@link Thing}s to search for the {@code id}
     * 
     * @return {@code true} if there is a {@link Thing} in {@code collection} with the identifier {@code id}
     */
    public static <T extends Thing> boolean isUuidInList(UUID id, List<T> collection) {
        for(T element: collection) {
            if (element.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
}
