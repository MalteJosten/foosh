package com.vs.foosh.api.model.web;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

/**
 * A class representing a HATEOAS-styled navigation.
 * @see https://en.wikipedia.org/wiki/HATEOAS
 * 
 * Such a representation consists of
 *     * a {@code relation} describing the relation to destination resource;
 *     * a {@code link} to the destination resource;
 *     * a {@code action} in form of a {@link HttpAction} to show the end-user which HTTP methods are available; and
 *     * a list of types which list the supported media types for the combination of destination resource, link, and action.
 * 
 * It implements the interface {@link Serializable} so it can be (de)serialized for saving and loading into and from persistent storage.
 */
public class LinkEntry implements Serializable {
    /**
     * The relation to the destination resource.
     */
    private String relation;

    /**
     * The link ({@link URI}) to the destination resource.
     */
    private URI link;

    /**
     * A {@link HttpAction} which is available at the {@code link}.
     */
    private HttpAction action;

    /**
     * A {@link List} of supported media types for the combination of the destination resource, link, and action.
     */
    private List<String> types;

    /**
     * Create a {@code LinkEntry} given a relation and link.
     * 
     * @param relation the relation to the destination resource
     * @param link the link ({@link URI}) to the destination resource
     */
    public LinkEntry(String relation, URI link) {
        this.relation = relation;
        this.link     = link;
    }

    /**
     * Create a {@code LinkEntry} given a relation, link, {@link HttpAction}, and a list of supported media types.
     * 
     * @param relation the relation to the destination resource
     * @param link the link ({@link URI}) to the destination resource
     * @param action an available {@link HttpAction}
     * @param types a {@link List} of supported media types as {@link String}s
     */
    public LinkEntry(String relation, URI link, HttpAction action, List<String> types) {
        this.relation = relation;
        this.link     = link;
        this.action   = action;
        this.types    = types;
    }

    /**
     * Return the relation.
     * 
     * @return the field {@code relation}
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Return the lin.
     * 
     * @return the field {@code lin}
     */
    public URI getLink() {
        return link;
    }

    /**
     * Return the http action.
     * 
     * @return the field {@code action}
     */
    public HttpAction getAction() {
        return action;
    }

    /**
     * Return the types.
     * 
     * @return the field {@code types}
     */
    public List<String> getTypes() {
        return types;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< LinkEntry >>\n");
        builder.append("Relation: " + relation + "\n");
        builder.append("Link:     " + link + "\n");
        builder.append("Action:   " + action.toString() + "\n");
        builder.append("Types:    " + types);

        return builder.toString();
    }
    
}
