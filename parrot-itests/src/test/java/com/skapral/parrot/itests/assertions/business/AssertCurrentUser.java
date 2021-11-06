package com.skapral.parrot.itests.assertions.business;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragmaticobjects.oo.tests.AssertCombined;
import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.assertions.json.AssertJsonHasFieldWithValue;
import com.skapral.parrot.itests.utils.authentication.Authentication;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpRequest;

public class AssertCurrentUser extends AssertHttp {
    public AssertCurrentUser(Authentication<DecodedJWT> authentication, URI uri, String expectedLogin, String expectedRole) {
        super(
                cli -> authentication.authenticate(HttpRequest.newBuilder())
                        .GET()
                        .uri(uri.resolve("/current"))
                        .build(),
                resp -> {
                    var jsonResp = new JSONObject(resp.body());
                    return new AssertCombined(
                            new StatusCode2XX(resp),
                            new AssertJsonHasFieldWithValue(jsonResp, "login", expectedLogin),
                            new AssertJsonHasFieldWithValue(jsonResp, "role", expectedRole)
                    );
                }
        );
    }
}
