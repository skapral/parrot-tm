package com.skapral.parrot.itests.utils.authentication;

import com.auth0.jwt.algorithms.Algorithm;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Authentication attempt with a token, expiration time of which has expired long time ago
 */
public class EldrichJwtTokenAuthorization extends ExplicitJwtTokenAuthentication {
    public EldrichJwtTokenAuthorization(String subject, String role, Algorithm algorithm) {
        super(
            builder -> builder
                    .withClaim("role", role)
                    .withSubject(subject)
                    .withExpiresAt(Date.from(Instant.EPOCH)),
            algorithm
        );
    }
}
