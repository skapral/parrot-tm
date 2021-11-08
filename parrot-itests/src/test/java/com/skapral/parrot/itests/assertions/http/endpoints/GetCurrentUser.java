package com.skapral.parrot.itests.assertions.http.endpoints;

import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class GetCurrentUser implements Endpoint {
    private final Authentication<?> authentication;
    private final URI authUri;

    public GetCurrentUser(Authentication<?> authentication, URI authUri) {
        this.authentication = authentication;
        this.authUri = authUri;
    }

    @Override
    public final HttpRequest request() {
        return authentication.authenticate(HttpRequest.newBuilder())
                .GET()
                .uri(authUri.resolve("/current"))
                .build();
    }
}
