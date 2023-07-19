package com.vs.foosh.api.model;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.vs.foosh.api.services.LinkBuilder;

public abstract class AbstractDevice implements Serializable {
    protected UUID id;
    protected String queryName;
    protected String deviceName;
    protected String type;
    protected AbstractDeviceDescription description;

    protected List<LinkEntry> links    = new ArrayList<>();
    protected List<LinkEntry> extLinks = new ArrayList<>();

    protected abstract void setObjectFields();

    public UUID getId() {
        return this.id;
    }

    public String getQueryName() {
        return this.queryName;
    }

    public void setQueryName(String name) {
        this.queryName = name.toLowerCase();
        if (this.links != null) {
            setLinks();
        }
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getType() {
        return this.type;
    }

    public AbstractDeviceDescription getDescription() {
        return this.description;
    }

    public void setDescription(AbstractDeviceDescription description) {
        this.description = description;
    }

    @JsonIgnore
    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    @JsonIgnore
    public List<LinkEntry> getExtLinks() {
        return this.extLinks;
    }

    public void setLinks() {
        if (links == null || links.isEmpty()) {
            links.clear();
            extLinks.clear();
        }

        links = buildSelfEntries();
        extLinks = DeviceList.getLinks("devices");
    }


    private List<LinkEntry> buildSelfEntries() {
        LinkEntry selfGet   = new LinkEntry("selfStatic", LinkBuilder.buildPath(List.of("device", this.id.toString())), HttpAction.GET, List.of());
        LinkEntry selfPatch = new LinkEntry("selfStatic", LinkBuilder.buildPath(List.of("device", this.id.toString())), HttpAction.PATCH, List.of("application/json"));

        LinkEntry queryGet   = new LinkEntry("selfQuery",  LinkBuilder.buildPath(List.of("device", this.queryName)), HttpAction.GET, List.of());
        LinkEntry queryPatch = new LinkEntry("selfQuery",  LinkBuilder.buildPath(List.of("device", this.queryName)), HttpAction.PATCH, List.of("application/json"));

        return new ArrayList<>(List.of(selfGet, selfPatch, queryGet, queryPatch));
    }

    @JsonIgnore
    public URI getStaticLink() {
        for (LinkEntry entry: links) {
            if (entry.getRelation().equals("selfStatic")) {
                return entry.getLink();
            }
        }

        return LinkBuilder.getRootLinkEntry().getLink();
    }

    @JsonIgnore
    public URI getQueryLink() {
        for (LinkEntry entry: links) {
            if (entry.getRelation().equals("selfQuery")) {
                return entry.getLink();
            }
        }

        return LinkBuilder.getRootLinkEntry().getLink();
    }

    @Override
    public String toString() {
        return "Device: " + id + "\nName: " + deviceName + "\nQuery-Name: " + queryName + "\nType: " + type
                + "\nlinks: " + links.toString();
    }
}