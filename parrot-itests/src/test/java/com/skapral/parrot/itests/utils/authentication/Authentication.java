package com.skapral.parrot.itests.utils.authentication;

import java.net.http.HttpRequest;
import java.util.Optional;

/**
 *
 * @param <Proof> artifact that proofs authentication successfulness
 */
public interface Authentication<Proof> {
    Optional<Proof> authenticate();
    HttpRequest.Builder authenticate(HttpRequest.Builder originalRequests);
}
