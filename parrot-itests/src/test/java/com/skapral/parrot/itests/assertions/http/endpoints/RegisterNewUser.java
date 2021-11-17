package com.skapral.parrot.itests.assertions.http.endpoints;

import com.skapral.parrot.itests.utils.authentication.Authentication;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpRequest;

public class RegisterNewUser implements Endpoint {
    private final Authentication<?> auth;
    private final URI usersUri;
    private final String login;
    private final String role;

    public RegisterNewUser(Authentication<?> auth, URI usersUri, String login, String role) {
        this.auth = auth;
        this.usersUri = usersUri;
        this.login = login;
        this.role = role;
    }

    @Override
    public final HttpRequest request() {
        return auth.authenticate(HttpRequest.newBuilder())
                .POST(HttpRequest.BodyPublishers.ofString(
                    new JSONObject(
                        HashMap.of(
                            "login", login,
                            "role", role
                        ).toJavaMap()
                    ).toString()
                ))
                .header("Content-Type", "application/json")
                .uri(usersUri.resolve("register"))
                .build();
    }
}
