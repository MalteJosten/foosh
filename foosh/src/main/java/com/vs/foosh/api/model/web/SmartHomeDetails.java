package com.vs.foosh.api.model.web;

import java.util.Map;

public class SmartHomeDetails {

    private String uri;
    private Map<String, String> details;

    public SmartHomeDetails() {
    }

    public SmartHomeDetails(String uri, Map<String, String> details) {
        setUri(uri);
        setDetails(details);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public Map<String, String> getDetails() {
        return this.details;
    }

    public boolean hasDetails() {
        return (this.details == null || this.details.isEmpty());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< SmartHomeDetails >>\n");
        if (this.details != null) {
            builder.append("details:\n");
            
            this.details.forEach((key, value) -> {
                builder.append("\t" + key + ": " + value + "\n");    
            });
        } else {
            builder.append("(- empty -)");
        }

        return builder.toString();
    }
}
