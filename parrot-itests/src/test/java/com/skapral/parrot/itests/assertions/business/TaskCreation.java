package com.skapral.parrot.itests.assertions.business;

import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class TaskCreation extends AssertHttp {
    public TaskCreation(URI uri, Authentication auth, String description) {
        super(
                cli -> auth.authenticate(HttpRequest.newBuilder())
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .uri(uri.resolve("/?description=TestTask"))
                        .build(),
                resp -> new StatusCode2XX(resp)
        );
    }
}
