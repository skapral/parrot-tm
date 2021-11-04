package com.skapral.parrot.itests.assertions.business;

import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.utils.authentication.Authentication;
import org.assertj.core.api.Assertions;

public class UserAuthentication implements Assertion {
    private final Authentication<?> authMethod;

    public UserAuthentication(Authentication<?> authMethod) {
        this.authMethod = authMethod;
    }

    @Override
    public final void check() throws Exception {
        Assertions.assertThat(authMethod.authenticate())
                .withFailMessage("Expecting user to be successfully authenticated")
                .isPresent();
    }
}
