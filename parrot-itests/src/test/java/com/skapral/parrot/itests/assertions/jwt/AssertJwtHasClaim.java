package com.skapral.parrot.itests.assertions.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.utils.authentication.Authentication;
import org.assertj.core.api.Assertions;

public class AssertJwtHasClaim implements Assertion {
    private final Authentication<DecodedJWT> jwtAuth;
    private final String expectedClaim;
    private final Object expectedValue;

    public AssertJwtHasClaim(Authentication<DecodedJWT> jwtAuth, String expectedClaim, Object expectedValue) {
        this.jwtAuth = jwtAuth;
        this.expectedClaim = expectedClaim;
        this.expectedValue = expectedValue;
    }

    @Override
    public final void check() throws Exception {
        var tokenOpt = jwtAuth.authenticate();
        Assertions.assertThat(tokenOpt).withFailMessage("Expecting JWT token, but it's absent").isPresent();
        Assertions.assertThat(tokenOpt.get().getClaim(expectedClaim).as(expectedValue.getClass())).isEqualTo(expectedValue);
    }
}
