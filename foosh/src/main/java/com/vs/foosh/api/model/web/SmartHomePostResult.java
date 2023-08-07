package com.vs.foosh.api.model.web;

import org.springframework.http.HttpStatusCode;

public class SmartHomePostResult {
    private int index;
    private HttpStatusCode statusCode;

    public SmartHomePostResult(int index, HttpStatusCode statusCode) {
        this.index      = index;
        this.statusCode = statusCode;
    }

    public int getIndex() {
        return this.index;
    }

    public HttpStatusCode getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< SmartHomePostResult >>\n");
        builder.append("Index:      " + index + "\n");
        builder.append("StatusCode: " + statusCode);

        return builder.toString();
    }
}
