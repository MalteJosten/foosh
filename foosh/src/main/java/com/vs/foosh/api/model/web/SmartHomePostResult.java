package com.vs.foosh.api.model.web;

import org.springframework.http.HttpStatusCode;

/**
 * A record containing the a link to a {@link SmartHomeInstruction} (in form of its {@code index}) and the {@link HttpStatusCode} which was acquired when executing the {@link SmartHomeInstruction}.
 * 
 * @param index the {@link SmartHomeInstruction}'s index
 * @param statusCode the smart home API's response's {@link HttpStatusCode}
 */
public record SmartHomePostResult(int index, HttpStatusCode statusCode) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< SmartHomePostResult >>\n");
        builder.append("Index:      " + index + "\n");
        builder.append("StatusCode: " + statusCode);

        return builder.toString();
    }
}
