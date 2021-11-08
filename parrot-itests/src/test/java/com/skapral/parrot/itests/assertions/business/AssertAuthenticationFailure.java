package com.skapral.parrot.itests.assertions.business;

import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode401;
import com.skapral.parrot.itests.assertions.http.endpoints.GetCurrentUser;
import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class AssertAuthenticationFailure extends AssertHttp {
    public AssertAuthenticationFailure(Authentication<?> auth, URI authUri) {
        super(
            new GetCurrentUser(auth, authUri),
            resp -> new StatusCode401(resp)
        );
    }
}
