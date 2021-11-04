package com.skapral.parrot.itests.utils.authentication;

import java.net.http.HttpRequest;
import java.util.Optional;

public class FakeAuthentication implements Authentication<Object> {
    private final String login;
    private final String role;

    public FakeAuthentication(String login, String role) {
        this.login = login;
        this.role = role;
    }

    @Override
    public final Optional<Object> authenticate() {
        // Do nothing
        return Optional.of(this);
    }

    @Override
    public final HttpRequest.Builder authenticate(HttpRequest.Builder originalRequest) {
        return originalRequest.header("Authorization", String.format("Mock %s:%s", login, role));
    }
}
