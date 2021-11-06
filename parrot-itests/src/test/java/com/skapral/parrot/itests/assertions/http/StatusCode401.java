package com.skapral.parrot.itests.assertions.http;

import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;

public class StatusCode401 implements Assertion {
    private final HttpResponse<?> response;

    public StatusCode401(HttpResponse<?> response) {
        this.response = response;
    }

    @Override
    public final void check() throws Exception {
        Assertions.assertThat(response.statusCode()).isEqualTo(401);
    }
}
