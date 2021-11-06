package com.skapral.parrot.itests.assertions.http.endpoints;

import com.skapral.parrot.itests.utils.authentication.Authentication;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpRequest;

public class RegisterUser implements Endpoint {
    private final Authentication<?> auth;
    private final URI authUri;
    private final String userName, userRole;

    public RegisterUser(Authentication<?> auth, URI authUri, String userName, String userRole) {
        this.auth = auth;
        this.authUri = authUri;
        this.userName = userName;
        this.userRole = userRole;
    }

    @Override
    public final HttpRequest request() {
        return auth.authenticate(HttpRequest.newBuilder())
                .POST(HttpRequest.BodyPublishers.ofString(request(userName, userRole)))
                .uri(authUri.resolve("/register"))
                .header("Content-Type", "application/json")
                .build();
    }

    private static String request(String userName, String userRole) {
        return new JSONObject()
                .put("login", userName)
                .put("role", userRole)
                .toString();
    }
}
