package com.skapral.parrot.itests.utils.authentication;

import java.net.http.HttpRequest;

public class FakeAuthentication implements Authentication {
    private final String login;
    private final String role;

    public FakeAuthentication(String login, String role) {
        this.login = login;
        this.role = role;
    }

    @Override
    public final void authenticate() {
        // Do nothing
    }

    @Override
    public final HttpRequest.Builder authenticate(HttpRequest.Builder originalRequest) {
        return originalRequest.header("Authorization", String.format("Mock %s:%s", login, role));
    }
}
