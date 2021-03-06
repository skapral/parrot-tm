package com.skapral.parrot.itests.assertions.http;

import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.assertions.http.endpoints.Endpoint;
import org.apache.commons.compress.archivers.sevenz.CLI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

public class AssertHttp implements Assertion {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private final Endpoint endpoint;
    private final Function<HttpResponse<String>, Assertion> responseAssertion;

    public AssertHttp(Endpoint endpoint, Function<HttpResponse<String>, Assertion> responseAssertion) {
        this.endpoint = endpoint;
        this.responseAssertion = responseAssertion;
    }

    @Override
    public final void check() throws Exception {
        var req = endpoint.request();
        var resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        var assertion = responseAssertion.apply(resp);
        assertion.check();
    }
}
