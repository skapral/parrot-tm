package com.skapral.parrot.itests.assertions.business;

import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.assertions.http.endpoints.RegisterUser;
import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;

public class UserRegistration extends AssertHttp {
    public UserRegistration(Authentication<?> auth, URI authUri, String userName, String userRole) {
        super(
            cli -> new RegisterUser(auth, authUri, userName, userRole).request(),
            resp -> new StatusCode2XX(resp)
        );
    }
}
