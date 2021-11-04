package com.skapral.parrot.itests.assertions.json;

import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;

public class AssertJsonHasFieldWithValue implements Assertion {
    private final JSONObject object;
    private final String field;
    private final Object value;

    public AssertJsonHasFieldWithValue(JSONObject object, String field, Object value) {
        this.object = object;
        this.field = field;
        this.value = value;
    }

    @Override
    public final void check() throws Exception {
        Assertions.assertThat(object).hasFieldOrPropertyWithValue(field, value);
    }
}
