package com.skapral.parrot.itests.assertions.http;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;
import java.util.function.Function;

public class AssertDecodingJwtTokenFromHeader implements Assertion {
    private final HttpResponse<?> response;
    private final Function<DecodedJWT, Assertion> assertion;

    public AssertDecodingJwtTokenFromHeader(HttpResponse<?> response, Function<DecodedJWT, Assertion> assertion) {
        this.response = response;
        this.assertion = assertion;
    }

    @Override
    public final void check() throws Exception {
        var authorizationOpt = response.headers().firstValue("Authorization");
        Assertions.assertThat(authorizationOpt).isPresent();
        var auth = authorizationOpt.get();
        var decodedJwt = JWT.decode(auth);
        assertion.apply(decodedJwt).check();
    }
}
