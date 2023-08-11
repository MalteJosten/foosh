package com.vs.foosh.api.model.device;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.IThingListObserver;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

public abstract class AbstractDevice extends Thing {
    protected String deviceName;
    protected String type;
    protected AbstractDeviceDescription description;

    protected List<LinkEntry> links    = new ArrayList<>();
    protected List<LinkEntry> extLinks = new ArrayList<>();

    protected List<IThingListObserver> observers = new ArrayList<>();

    protected abstract void setObjectFields();

    public AbstractDevice(String name) {
        this.id   = UUID.randomUUID();
        this.name = name;
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

    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    public List<LinkEntry> getSelfStaticLinks(String label) {
        List<LinkEntry> links = new ArrayList<>();
       
        for(LinkEntry link: getSelfLinks()) {
            if (link.getRelation().equals("selfStatic")) {
                links.add(new LinkEntry(label, link.getLink(), link.getAction(), link.getTypes()));
            }
        }

        return links;
    }

    public List<LinkEntry> getExtLinks() {
        return this.extLinks;
    }

    public void setLinks() {
        if (!links.isEmpty()) {
            links.clear();
            extLinks.clear();
        }

        links = buildSelfEntries();
        extLinks = ListService.getDeviceList().getLinks("devices");
    }


    private List<LinkEntry> buildSelfEntries() {
        LinkEntry selfGet   = new LinkEntry("selfStatic", LinkBuilder.getDeviceLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry selfPatch = new LinkEntry("selfStatic", LinkBuilder.getDeviceLink(this.id.toString()), HttpAction.PATCH, List.of("application/json"));

        LinkEntry queryGet   = new LinkEntry("selfName",  LinkBuilder.getDeviceLink(this.name), HttpAction.GET, List.of());

        return new ArrayList<>(List.of(selfGet, selfPatch, queryGet));
    }

    public URI getStaticLink() {
        for (LinkEntry entry: links) {
            if (entry.getRelation().equals("selfStatic")) {
                return entry.getLink();
            }
        }

        return LinkBuilder.getRootLinkEntry().getLink();
    }

    public URI getQueryLink() {
        for (LinkEntry entry: links) {
            if (entry.getRelation().equals("selfName")) {
                return entry.getLink();
            }
        }

        return LinkBuilder.getRootLinkEntry().getLink();
    }

    public DeviceDisplayRepresentation getDisplayRepresentation() {
        return new DeviceDisplayRepresentation(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< Device >>\n");
        builder.append("Device:      " + id + "\n");
        builder.append("Device-Name: " + deviceName + "\n");
        builder.append("Query-Name:  " + name + "\n");
        builder.append("Type:        " + type + "\n");
        builder.append("SelfLinks:\n");
        for(LinkEntry link: links) {
            builder.append("\trelation: " + link.getRelation() + "\n");
            builder.append("\tlink:     " + link.getLink() + "\n");
            builder.append("\taction:   " + link.getAction() + "\n");
            builder.append("\ttypes:    " + link.getTypes() + "\n");
        }
        builder.append("ExtLinks:\n");
        for(LinkEntry link: extLinks) {
            builder.append("\trelation: " + link.getRelation() + "\n");
            builder.append("\tlink:     " + link.getLink() + "\n");
            builder.append("\taction:   " + link.getAction() + "\n");
            builder.append("\ttypes:    " + link.getTypes() + "\n");
        }

        return builder.toString();
    }
}