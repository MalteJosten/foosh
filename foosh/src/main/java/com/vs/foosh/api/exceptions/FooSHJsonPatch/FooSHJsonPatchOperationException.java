package com.vs.foosh.api.exceptions.FooSHJsonPatch;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.vs.foosh.api.model.web.LinkEntry;

public class FooSHJsonPatchOperationException extends RuntimeException {
    private UUID thingId;
    private List<LinkEntry> links;

    public FooSHJsonPatchOperationException(UUID thingId, List<LinkEntry> links, String message) {
        super(message);
        this.thingId    = thingId;
        this.links      = links;
    }

    public UUID getThingId() {
        return this.thingId;
    }

    public List<LinkEntry> getLinks() {
        return this.links;
    }

    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    };
    
}
