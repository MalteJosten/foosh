package com.vs.foosh.api.model.web;

import java.util.Map;

/**
 * A data class containing details concerning the used smart home API.
 */
public class SmartHomeDetails {

    /**
     * The uri/url under which the smart home API can be reached.
     */
    private String uri;

    /**
     * A collection of further smart home details.
     * These could include authentication credentials or other connection-related information.
     */
    private Map<String, String> details;

    /**
     * Create an empty {@code SmartHomeDetails} instance.
     */
    public SmartHomeDetails() {
    }

    /**
     * Create a {@code SmartHomeDetails} instance.
     * 
     * @param uri the uri
     * @param details a {@link Map} of details as {@link String}-{@link String} pairs
     */
    public SmartHomeDetails(String uri, Map<String, String> details) {
        setUri(uri);
        setDetails(details);
    }

    /**
     * Set the field {@code uri}.
     * 
     * @param uri the new uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Return the smart home uri/url.
     * 
     * @return the field {@code uri}
     */
    public String getUri() {
        return this.uri;
    }

    /**
     * Set the field {@code details}.
     * 
     * @param details the new {@link Map} containing {@link String}-{@link String} pairs
     */
    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    /**
     * Return the details.
     * 
     * @return the field {@code details}
     */
    public Map<String, String> getDetails() {
        return this.details;
    }

    /**
     * Return {@code true} if the field {@code details} is not empty.
     */
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
