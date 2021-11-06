package com.skapral.parrot.itests.utils.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.function.Function;

public class ExplicitJwtTokenAuthentication implements Authentication<DecodedJWT> {
    private final Function<JWTCreator.Builder, JWTCreator.Builder> jwtConstruction;
    private final Algorithm signingAlgorithm;

    public ExplicitJwtTokenAuthentication(Function<JWTCreator.Builder, JWTCreator.Builder> jwtConstruction, Algorithm signingAlgorithm) {
        this.jwtConstruction = jwtConstruction;
        this.signingAlgorithm = signingAlgorithm;
    }

    @Override
    public final Optional<DecodedJWT> authenticate() {
        return Optional.of(JWT.decode(jwtToken()));
    }

    @Override
    public final HttpRequest.Builder authenticate(HttpRequest.Builder originalRequests) {
        return originalRequests.header("Authorization", "Bearer " + jwtToken());
    }

    private String jwtToken() {
        return jwtConstruction.apply(JWT.create()).sign(signingAlgorithm);
    }
}
