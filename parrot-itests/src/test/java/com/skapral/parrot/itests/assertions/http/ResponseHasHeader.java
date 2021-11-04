package com.skapral.parrot.itests.assertions.http;

import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;

public class ResponseHasHeader implements Assertion {
    private final HttpResponse<?> response;
    private final String header;

    public ResponseHasHeader(HttpResponse<?> response, String header) {
        this.response = response;
        this.header = header;
    }

    @Override
    public final void check() throws Exception {
        Assertions.assertThat(response.headers().firstValue(header)).isPresent();
    }
}
