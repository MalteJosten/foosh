package com.vs.foosh.api.model.web;

import java.util.HashMap;

public class SmartHomeCredentials {

    private String uri;
    private HashMap<String, String> credentials;

    public SmartHomeCredentials() {
    }

    public SmartHomeCredentials(String uri, HashMap<String, String> credentials) {
        setUri(uri);
        setCredentials(credentials);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }

    public void setCredentials(HashMap<String, String> credentials) {
        this.credentials = credentials;
    }

    public HashMap<String, String> getCredentials() {
        return this.credentials;
    }

    public boolean hasCredentials() {
        return (this.credentials == null || this.credentials.isEmpty());
    }

    // Modify class name header
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SmartHomeCredentials\n");
        if (this.credentials != null) {
            builder.append("\tCredentials:\n");
            
            this.credentials.forEach((key, value) -> {
                builder.append("\t\t" + key + ": " + value + "\n");    
            });
        } else {
            builder.append("\t(- empty -)");
        }

        return builder.toString();
    }
}
