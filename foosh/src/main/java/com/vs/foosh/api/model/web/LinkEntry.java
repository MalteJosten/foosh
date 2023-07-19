package com.vs.foosh.api.model.web;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class LinkEntry implements Serializable {
    private String relation;
    private URI link;
    private HttpAction action;
    private List<String> types;

    public LinkEntry(String relation, URI link) {
        this.relation = relation;
        this.link     = link;
    }

    public LinkEntry(String relation, URI link, HttpAction action, List<String> types) {
        this.relation = relation;
        this.link     = link;
        this.action   = action;
        this.types    = types;
    }

    public String getRelation() {
        return relation;
    }

    public URI getLink() {
        return link;
    }

    public HttpAction getAction() {
        return action;
    }

    public List<String> getTypes() {
        return types;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("relation:\t" + relation + "\n");
        builder.append("link:\t" + link + "\n");
        builder.append("action:\t" + action.toString() + "\n");
        builder.append("types:\n");
        for (String type: types) {
            builder.append("\t\t" + type);
        }

        return builder.toString();
    }
    
}
