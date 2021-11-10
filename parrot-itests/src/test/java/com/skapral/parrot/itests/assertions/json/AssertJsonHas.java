package com.skapral.parrot.itests.assertions.json;

import com.pragmaticobjects.oo.tests.Assertion;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

public class AssertJsonHas implements Assertion {
    private final String actualJson;
    private final String assertedPart;
    private final JSONCompareMode mode;

    public AssertJsonHas(String actualJson, String assertedPart, JSONCompareMode mode) {
        this.actualJson = actualJson;
        this.assertedPart = assertedPart;
        this.mode = mode;
    }

    @Override
    public final void check() throws Exception {
        JSONAssert.assertEquals(
            assertedPart,
            actualJson,
            new DefaultComparator(mode) {
                @Override
                public final void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
                    if(expectedValue.toString().equals("_")) {
                        result.passed();
                    } else {
                        super.compareValues(prefix, expectedValue, actualValue, result);
                    }
                }

                /*@Override
                protected void compareJSONArrayOfSimpleValues(String key, JSONArray expected, JSONArray actual, JSONCompareResult result) throws JSONException {
                    Map<Object, Integer> expectedCount = JSONCompareUtil.getCardinalityMap(jsonArrayToList(expected));
                    var freeSlots = expectedCount.remove("_");
                    var misplacementsFound = 0;
                    Map<Object, Integer> actualCount = JSONCompareUtil.getCardinalityMap(jsonArrayToList(actual));
                    for (Object o : expectedCount.keySet()) {
                        if (!actualCount.containsKey(o)) {
                            result.missing(key + "[]", o);
                        } else if (!actualCount.get(o).equals(expectedCount.get(o))) {
                            misplacementsFound += actualCount.get(o)

                            result.fail(key + "[]: Expected " + expectedCount.get(o) + " occurrence(s) of " + o + " but got " + actualCount.get(o) + " occurrence(s)");
                        }
                    }
                    for (Object o : actualCount.keySet()) {
                        if (!expectedCount.containsKey(o)) {
                            freeSlots--;
                            if(freeSlots<0) {
                                result.unexpected(key + "[]", o);
                            }
                        }
                    }
                }*/
            }
        );
    }
}
