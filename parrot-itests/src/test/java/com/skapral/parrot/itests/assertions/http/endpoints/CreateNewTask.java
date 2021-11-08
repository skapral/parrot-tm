package com.skapral.parrot.itests.assertions.http.endpoints;

import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class CreateNewTask implements Endpoint {
    private final Authentication<?> auth;
    private final URI tasksUri;
    private final String description;

    public CreateNewTask(Authentication<?> auth, URI tasksUri, String description) {
        this.auth = auth;
        this.tasksUri = tasksUri;
        this.description = description;
    }

    @Override
    public final HttpRequest request() {
        return auth.authenticate(HttpRequest.newBuilder())
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(tasksUri.resolve("/?description=" + description))
                .build();
    }
}
