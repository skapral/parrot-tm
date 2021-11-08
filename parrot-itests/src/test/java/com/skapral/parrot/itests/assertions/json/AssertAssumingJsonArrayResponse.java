package com.skapral.parrot.itests.assertions.json;

import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.function.Function;

public class AssertAssumingJsonArrayResponse implements Assertion {
    private final HttpResponse<?> response;
    private final Function<JSONArray, Assertion> assertions;

    public AssertAssumingJsonArrayResponse(HttpResponse<?> response, Function<JSONArray, Assertion> assertions) {
        this.response = response;
        this.assertions = assertions;
    }

    @Override
    public final void check() throws Exception {
        final var body = response.body();
        final JSONArray array;
        try {
            array = new JSONArray(body.toString());
        } catch(JSONException ex) {
            Assertions.fail("Expecting body to be a valid JSON: " + body, ex);
            throw ex;
        }
        assertions.apply(array).check();
    }
}
