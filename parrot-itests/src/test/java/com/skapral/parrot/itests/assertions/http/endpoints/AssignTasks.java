package com.skapral.parrot.itests.assertions.http.endpoints;

import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class AssignTasks implements Endpoint {
    private final Authentication<?> authentication;
    private final URI tasksUri;

    public AssignTasks(Authentication<?> authentication, URI tasksUri) {
        this.authentication = authentication;
        this.tasksUri = tasksUri;
    }

    @Override
    public final HttpRequest request() {
        return authentication.authenticate(HttpRequest.newBuilder())
            .POST(HttpRequest.BodyPublishers.noBody())
            .uri(tasksUri.resolve("/assign"))
            .build();
    }
}
