package com.skapral.parrot.itests.assertions.business;

import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;

import java.net.URI;
import java.net.http.HttpRequest;

public class TaskCreation extends AssertHttp {
    public TaskCreation(URI uri, String role, String description) {
        super(
                cli -> HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .header("Authorization", "Mock testuser:" + role)
                        .uri(uri.resolve("/?description=TestTask"))
                        .build(),
                resp -> new StatusCode2XX(resp)
        );
    }
}
