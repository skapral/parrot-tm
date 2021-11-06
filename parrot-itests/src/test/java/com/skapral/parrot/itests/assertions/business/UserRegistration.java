package com.skapral.parrot.itests.assertions.business;

import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.utils.authentication.Authentication;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.net.URI;
import java.net.http.HttpRequest;

public class UserRegistration extends AssertHttp {
    public UserRegistration(Authentication<?> auth, URI authUri, String userName, String userRole) {
        super(
            cli -> auth.authenticate(HttpRequest.newBuilder())
                    .POST(HttpRequest.BodyPublishers.ofString(request(userName, userRole)))
                    .uri(authUri.resolve("/register"))
                    .header("Content-Type", "application/json")
                    .build(),
            resp -> new StatusCode2XX(resp)
        );
    }

    public static String request(String userName, String userRole) {
        return new JSONObject()
                .put("login", userName)
                .put("role", userRole)
                .toString();
    }
}
