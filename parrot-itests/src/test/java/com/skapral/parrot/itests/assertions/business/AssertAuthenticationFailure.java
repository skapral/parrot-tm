package com.skapral.parrot.itests.assertions.business;

import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode401;
import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class AssertAuthenticationFailure extends AssertHttp {
    public AssertAuthenticationFailure(Authentication<?> auth, URI uri) {
        super(
            cli -> auth.authenticate(HttpRequest.newBuilder())
                .GET()
                .uri(uri)
                .build(),
            resp -> new StatusCode401(resp)
        );
    }
}
