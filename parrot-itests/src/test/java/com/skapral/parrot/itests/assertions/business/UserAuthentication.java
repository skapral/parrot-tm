package com.skapral.parrot.itests.assertions.business;

import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.utils.authentication.Authentication;

public class UserAuthentication implements Assertion {
    private final Authentication authMethod;

    public UserAuthentication(Authentication authMethod) {
        this.authMethod = authMethod;
    }

    @Override
    public final void check() throws Exception {
        // assuming that assertions are the part of `Authentication`'s implementations here
        authMethod.authenticate();
    }
}
