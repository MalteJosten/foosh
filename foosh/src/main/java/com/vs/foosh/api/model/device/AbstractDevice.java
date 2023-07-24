package com.vs.foosh.api.model.device;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.HttpResponseObject;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;

public abstract class AbstractDevice extends HttpResponseObject implements Serializable {
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
    public List<LinkEntry> getSelfStaticLinks(String label) {
        List<LinkEntry> links = new ArrayList<>();
       
        for(LinkEntry link: getSelfLinks()) {
            if (link.getRelation().equals("selfStatic")) {
                links.add(new LinkEntry(label, link.getLink(), link.getAction(), link.getTypes()));
            }
        }

        return links;
    }

    @JsonIgnore
    public List<LinkEntry> getExtLinks() {
        return this.extLinks;
    }

    public void setLinks() {
        if (!links.isEmpty()) {
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
        StringBuilder builder = new StringBuilder();
        builder.append("Device: " + id + "\n");
        builder.append("Name: " + deviceName + "\n");
        builder.append("Query-Name: " + queryName + "\n");
        builder.append("Type: " + type + "\n");
        builder.append("SelfLinks:\n");
        for(LinkEntry link: links) {
            builder.append("\trelation:\t" + link.getRelation() + "\n");
            builder.append("\tlink:\t\t"   + link.getLink() + "\n");
            builder.append("\taction:\t\t" + link.getAction() + "\n");
            builder.append("\ttypes:\t\t"  + link.getTypes() + "\n");
        }
        builder.append("ExtLinks:\n");
        for(LinkEntry link: extLinks) {
            builder.append("\trelation:\t" + link.getRelation() + "\n");
            builder.append("\tlink:\t\t"   + link.getLink() + "\n");
            builder.append("\taction:\t\t" + link.getAction() + "\n");
            builder.append("\ttypes:\t\t"  + link.getTypes() + "\n");
        }

        return builder.toString();
    }
}