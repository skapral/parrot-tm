package com.skapral.parrot.itests.assertions.business;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragmaticobjects.oo.tests.AssertCombined;
import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.assertions.http.endpoints.GetCurrentUser;
import com.skapral.parrot.itests.assertions.json.AssertJsonHas;
import com.skapral.parrot.itests.utils.authentication.Authentication;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.net.URI;

public class AssertCurrentUser extends AssertHttp {
    public AssertCurrentUser(Authentication<DecodedJWT> authentication, URI authUri, String expectedLogin, String expectedRole) {
        super(
            new GetCurrentUser(authentication, authUri),
            resp -> new AssertCombined(
                new StatusCode2XX(resp),
                new AssertJsonHas(
                    resp.body(),
                    String.format(
                        "{\"login\": \"%s\", \"role\": \"%s\"}",
                        expectedLogin,
                        expectedRole
                    ),
                    JSONCompareMode.LENIENT
                )
            )
        );
    }
}
