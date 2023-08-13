package com.vs.foosh.api.model.device;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.IThingListObserver;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

/**
 * A subclass of {@link Thing} that adds support for smart home devices.
 * It adds an additional {@code deviceName}, {@code type}, and {@link AbstractDeviceDescription} fields
 * which add data to the object, retrieved from the smart home API.
 */
public abstract class AbstractDevice extends Thing {
    protected String deviceName;
    protected String type;
    protected AbstractDeviceDescription description;

    protected List<LinkEntry> links    = new ArrayList<>();
    protected List<LinkEntry> extLinks = new ArrayList<>();

    protected List<IThingListObserver> observers = new ArrayList<>();

    protected abstract void setObjectFields();

    /**
     * Create an {@code AbstractDevice} with a {@code name} and generate an UUID using
     * {@link java.util.UUID#randomUUID() randomUUID()}.
     * 
     * @param name the string representation of the name
     */
    public AbstractDevice(String name) {
        this.id   = UUID.randomUUID();
        this.name = name;
    }

    /**
     * Set/Overwrite the field {@code name} with the parameter {@code name}.
     * Before setting the name, it is transformed into its lowercase representation.
     * 
     * @param name the string representation of the new name
     */
    public void setName(String name) {
        this.name = name.toLowerCase();
        if (this.links != null) {
            setLinks();
        }
    }

    /**
     * Return the device name.
     * 
     * @return the value of {@code deviceName} as a {@link String}
     */
    public String getDeviceName() {
        return this.deviceName;
    }

    /**
     * Return the device type.
     * 
     * @return the value of {@code type} as a {@link String}
     */
    public String getType() {
        return this.type;
    }

    /**
     * Return the device description.
     * 
     * @return the value of {@code description} as an {@link AbstractDeviceDescription}
     */
    public AbstractDeviceDescription getDescription() {
        return this.description;
    }

    /**
     * Set/Overwrite the field {@code description}.
     * 
     * @param description the new description as an {@link AbstractDeviceDescription}
     */
    public void setDescription(AbstractDeviceDescription description) {
        this.description = description;
    }

    /**
     * Return the list of self-links.
     * These include the {@code selfStatic} and {@code selfName} links to itself.
     * 
     * @return the field {@code links} as a list of {@link LinkEntry} 
     */
    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    /**
     * Return the list of static self-links.
     * These only include the {@code selfStatic} links to itself.
     * 
     * @return each {@link LinkEntry} of {@code links} that have the label {@code selfStatic}
     */
    public List<LinkEntry> getSelfStaticLinks(String label) {
        List<LinkEntry> links = new ArrayList<>();
       
        for(LinkEntry link: getSelfLinks()) {
            if (link.getRelation().equals("selfStatic")) {
                links.add(new LinkEntry(label, link.getLink(), link.getAction(), link.getTypes()));
            }
        }

        return links;
    }

    /**
     * Return the list of external links.
     * These include the links to the corresponding {@link ThingList}.  
     * 
     * @return the field {@code extLinks} as a list of {@link  LinkEntry}
     */
    public List<LinkEntry> getExtLinks() {
        return this.extLinks;
    }

    /**
     * Set/Overwrite both link {@link List}s, {@code links} and {@code extLinks}, of type {@link LinkEntry}.
     * {@code links} is set to the return value of {@link com.vs.foosh.api.model.device.AbstractDevice#buildSelfEntries() buildSelfEntries()}.
     * {@code extLinks} are set by retrieving the corresponding collection from the {@link ListService} or the {@link DeviceList}, to be more exact.
     */
    public void setLinks() {
        if (!links.isEmpty()) {
            links.clear();
            extLinks.clear();
        }

        links = buildSelfEntries();
        extLinks = ListService.getDeviceList().getLinks("devices");
    }

    /**
     * (Re-)Build the collection of self links, including both {@code selfStatic} and {@code selfName}.
     * They containing a {@link LinkEntry} for the HTTP methods GET and PATCH.
     * 
     * @return the {@link List} of built self links as a {@link List} of type {@link LinkEntry}
     */
    private List<LinkEntry> buildSelfEntries() {
        LinkEntry selfGet   = new LinkEntry("selfStatic", LinkBuilder.getDeviceLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry selfPatch = new LinkEntry("selfStatic", LinkBuilder.getDeviceLink(this.id.toString()), HttpAction.PATCH, List.of("application/json-patch+json"));

        LinkEntry queryGet   = new LinkEntry("selfName",  LinkBuilder.getDeviceLink(this.name), HttpAction.GET, List.of());

        return new ArrayList<>(List.of(selfGet, selfPatch, queryGet));
    }

    /**
     * Return the display representation of the device.
     * 
     * @see {@link DeviceDisplayRepresentation}
     *
     * @return the {@link DeviceDisplayRepresenatation} of the device
     */
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