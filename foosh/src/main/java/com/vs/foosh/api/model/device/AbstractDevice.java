package com.vs.foosh.api.model.device;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;

public abstract class AbstractDevice extends Thing implements Serializable {
    protected String deviceName;
    protected String type;
    protected AbstractDeviceDescription description;

    protected List<LinkEntry> links    = new ArrayList<>();
    protected List<LinkEntry> extLinks = new ArrayList<>();

    protected abstract void setObjectFields();

    public AbstractDevice() {
        super();
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
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

        LinkEntry queryGet   = new LinkEntry("selfQuery",  LinkBuilder.buildPath(List.of("device", this.name)), HttpAction.GET, List.of());
        LinkEntry queryPatch = new LinkEntry("selfQuery",  LinkBuilder.buildPath(List.of("device", this.name)), HttpAction.PATCH, List.of("application/json"));

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
        builder.append("Device-Name: " + deviceName + "\n");
        builder.append("Query-Name: " + name + "\n");
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