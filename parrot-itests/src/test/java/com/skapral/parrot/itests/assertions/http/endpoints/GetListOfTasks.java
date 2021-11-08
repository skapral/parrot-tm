package com.skapral.parrot.itests.assertions.http.endpoints;


import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class GetListOfTasks implements Endpoint {
    private final Authentication<?> auth;
    private final URI tasksUri;

    public GetListOfTasks(Authentication<?> auth, URI tasksUri) {
        this.auth = auth;
        this.tasksUri = tasksUri;
    }

    @Override
    public final HttpRequest request() {
        return auth.authenticate(HttpRequest.newBuilder())
                .uri(tasksUri)
                .GET()
                .build();
    }
}
