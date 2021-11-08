package com.skapral.parrot.itests.assertions.json;

import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.function.Function;

public class AssertAssumingJsonResponse implements Assertion {
    private final HttpResponse<?> response;
    private final Function<JSONObject, Assertion> assertions;

    public AssertAssumingJsonResponse(HttpResponse<?> response, Function<JSONObject, Assertion> assertions) {
        this.response = response;
        this.assertions = assertions;
    }

    @Override
    public final void check() throws Exception {
        final var body = response.body();
        final JSONObject object;
        try {
            object = new JSONObject(body.toString());
        } catch(JSONException ex) {
            Assertions.fail("Expecting body to be a valid JSON: " + body, ex);
            throw ex;
        }
        assertions.apply(object).check();
    }
}
