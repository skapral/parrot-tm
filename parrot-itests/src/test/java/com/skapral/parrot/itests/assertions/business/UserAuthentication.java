package com.skapral.parrot.itests.assertions.business;

import com.pragmaticobjects.oo.tests.AssertCombined;
import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.ResponseHasHeader;
import com.skapral.parrot.itests.assertions.http.StatusCodeRedirect;

import java.net.URI;
import java.net.http.HttpRequest;

public class UserAuthentication extends AssertHttp {
    public UserAuthentication(URI authUri, String login) {
        super(
            cli -> HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .uri(authUri.resolve("/auth/login?login=" + login))
                        .build(),
            resp -> new AssertCombined(
                    new StatusCodeRedirect(resp),
                    new ResponseHasHeader(resp, "Authorization")
            )
        );
    }
}
