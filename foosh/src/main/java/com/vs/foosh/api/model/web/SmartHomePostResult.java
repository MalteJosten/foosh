package com.vs.foosh.api.model.web;

import org.springframework.http.HttpStatusCode;

public record SmartHomePostResult(int index, HttpStatusCode statusCode) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< SmartHomePostResult >>\n");
        builder.append("Index:      " + index + "\n");
        builder.append("StatusCode: " + statusCode);

        return builder.toString();
    }
}
