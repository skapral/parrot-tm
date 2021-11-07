package com.skapral.parrot.itests.assertions.json;

import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;

public class AssertJsonArraySize implements Assertion {
    private final JSONArray array;
    private final int size;

    public AssertJsonArraySize(JSONArray array, int size) {
        this.array = array;
        this.size = size;
    }

    @Override
    public final void check() throws Exception {
        Assertions.assertThat(array.length()).isEqualTo(size);
    }
}
